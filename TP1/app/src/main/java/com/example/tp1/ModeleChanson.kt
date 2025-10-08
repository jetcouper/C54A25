package com.example.tp1

import android.content.Context
import android.widget.SimpleAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.example.atelier3observerpattern.ObservateurChangement
import com.example.atelier3observerpattern.Sujet
import org.json.JSONArray
import org.json.JSONObject

class ModeleChanson(context: Context) : Sujet {
    val url = "https://api.jsonbin.io/v3/b/67fe6a908a456b796689f63d?meta=false"


    init {
        initialiserListe(context)
    }

    private fun initialiserListe(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val li:ListeMusiques = Klaxon().parse<ListeMusiques>(response) ?: ListeMusiques()
                val jsonObject = JSONObject(response)
                val jsonarray = jsonObject.getJSONArray("music")
                decomposerReponse(jsonarray)
                Toast.makeText(context,"Response is: ${li.articles.size}", Toast.LENGTH_LONG).show()
            },
            {
                Toast.makeText(context,"Erreur", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)
    }
    fun decomposerReponse(tab: JSONArray)
    {
        val remplir = ArrayList<HashMap<String,Any>>()

        for (i in 0..tab.length() -1)
        {
            val itemMap = HashMap<String, Any>()
            itemMap.put("nom",tab.getJSONObject(i).get("nom").toString())
            itemMap.put("prix",tab.getJSONObject(i).get("prix").toString() + " $")
            remplir.add(itemMap)
        }


        //A faire
        //val from = arrayOf("nom", "prix")
        //val to = intArrayOf(R.id.txtNom, R.id.txtPrix)
        //val adapter = SimpleAdapter(this,remplir,R.layout.layoutlist,from,to)
        //liste.setAdapter(adapter)
    }


    override fun ajouterObservateur(o: ObservateurChangement) {
        TODO("Not yet implemented")
    }

    override fun enleverObservateur(o: ObservateurChangement) {
        TODO("Not yet implemented")
    }

    override fun avertirObservateurs() {
        TODO("Not yet implemented")
    }
}