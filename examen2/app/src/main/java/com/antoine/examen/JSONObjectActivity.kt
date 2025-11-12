package com.antoine.examen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.beust.klaxon.Klaxon
import org.json.JSONArray
import android.text.format.DateUtils


@SuppressLint("StaticFieldLeak")
object JSONObjectActivity {
    val url = "https://api.jsonbin.io/v3/b/6908bbe4d0ea881f40d109b6?meta=false"
    var contexte: Context ?= null
    var listemusique = ArrayList<HashMap<String, Any>>()

    fun init (context: Context) {
        initialiserListe(context)
    }

    private fun initialiserListe(context: Context) {
        contexte = context
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val li:KlaxonChanson = Klaxon().parse<KlaxonChanson>(response) ?: KlaxonChanson()
                val jsonObject = JSONObject(response)
                val jsonarray = jsonObject.getJSONArray("items")
                listemusique = decomposerReponse(jsonarray)
                //Avertir l'activité avec l'observateur, après le changement de la liste
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
            itemMap.put("nom",tab.getJSONObject(i).get("nom").toString())
            itemMap.put("date_sortie",tab.getJSONObject(i).get("date_sortie").toString())
            itemMap.put("nb_chansons",tab.getJSONObject(i).get("nb_chansons"))
            itemMap.put("type",tab.getJSONObject(i).get("type").toString())

            remplir.add(itemMap)
        }
        return remplir
    }
    fun retourListeMusique(): ArrayList<HashMap<String, Any>> {
        return listemusique
    }


}