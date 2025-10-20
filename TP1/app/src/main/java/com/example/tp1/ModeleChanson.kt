package com.example.tp1

import android.annotation.SuppressLint
import android.content.Context
import android.widget.SimpleAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.example.tp1.Sujet
import org.json.JSONArray
import org.json.JSONObject
import android.text.format.DateUtils

@SuppressLint("StaticFieldLeak")
object ModeleChanson : Sujet {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    var contexte: Context ?= null
    var listemusique = ArrayList<HashMap<String, Any>>()
    private val observateurs = mutableListOf<ObservateurChangement>()

    fun init (context: Context) {
        initialiserListe(context)
    }

    private fun initialiserListe(context: Context) {
        contexte = context
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val li:ListeMusiques = Klaxon().parse<ListeMusiques>(response) ?: ListeMusiques()
                val jsonObject = JSONObject(response)
                val jsonarray = jsonObject.getJSONArray("music")
                listemusique = decomposerReponse(jsonarray)
                //Avertir l'activité avec l'observateur, après le changement de la liste
                avertirObservateurs()
                Toast.makeText(context,"Liste de ${li.musiques.size} musique.", Toast.LENGTH_LONG).show()
            },
            {
                Toast.makeText(context,"Erreur", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)
    }
    fun decomposerReponse(tab: JSONArray): ArrayList<HashMap<String,Any>> {
        val remplir = ArrayList<HashMap<String,Any>>()

        for (i in 0..tab.length() -1)
        {
            val itemMap = HashMap<String, Any>()
            itemMap.put("id",tab.getJSONObject(i).get("id").toString())
            itemMap.put("title",tab.getJSONObject(i).get("title").toString())
            itemMap.put("album",tab.getJSONObject(i).get("album").toString())
            itemMap.put("artist",tab.getJSONObject(i).get("artist").toString())
            itemMap.put("genre",tab.getJSONObject(i).get("genre").toString())
            itemMap.put("source",tab.getJSONObject(i).get("source").toString())
            itemMap.put("image",tab.getJSONObject(i).get("image").toString())
            itemMap.put("trackNumber",tab.getJSONObject(i).get("trackNumber"))
            itemMap.put("totalTrackCount",tab.getJSONObject(i).get("totalTrackCount"))
            itemMap.put("duration", DateUtils.formatElapsedTime(tab.getJSONObject(i).getInt("duration").toLong()))
            itemMap.put("site",tab.getJSONObject(i).get("site").toString())
            remplir.add(itemMap)
        }
        return remplir
    }



    fun retourListeMusique(): ArrayList<HashMap<String, Any>> {
        return listemusique
    }

    override fun ajouterObservateur(o: ObservateurChangement) {
        observateurs.add(o)
    }

    override fun enleverObservateur(o: ObservateurChangement) {
        observateurs.remove(o)
    }

    override fun avertirObservateurs() {
        for(obs in observateurs){
            obs.changement(listemusique.size)
        }
    }
}