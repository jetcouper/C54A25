package com.example.appvolley

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import org.json.JSONArray
import org.json.JSONObject

class KlaxonActivity : AppCompatActivity() {
    val url = "https://api.jsonbin.io/v3/b/67fe6a908a456b796689f63d?meta=false"
    lateinit var liste : ListView
    //var v:ArrayList<HashMap<String,Any>> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jsonobject)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val queue = Volley.newRequestQueue(this)
        liste = findViewById(R.id.listProduit)

        val ec = Ecouteur()

        liste.onItemClickListener = ec
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val li:ListeProduits = Klaxon().parse<ListeProduits>(response) ?: ListeProduits()
                val jsonObject = JSONObject(response)
                val jsonarray = jsonObject.getJSONArray("accessoires")
                decomposerReponse(jsonarray)
                Toast.makeText(this@KlaxonActivity,"Response is: ${li.articles.size}",Toast.LENGTH_LONG).show()
            },
            { Toast.makeText(this@KlaxonActivity,"Erreur",Toast.LENGTH_LONG).show() })
        queue.add(stringRequest)

        //System.out.print("Quand ce sera fait")

    }
    fun decomposerReponse(tab: JSONArray)
    {
        val remplir = ArrayList<HashMap<String,String>>()

        for (i in 0..tab.length() -1)
        {
            val itemMap = HashMap<String, String>()
            itemMap.put("nom",tab.getJSONObject(i).get("nom").toString())
            itemMap.put("prix",tab.getJSONObject(i).get("prix").toString() + " $")
            remplir.add(itemMap)
        }



        val from = arrayOf("nom", "prix")
        val to = intArrayOf(R.id.txtNom, R.id.txtPrix)
        val adapter = SimpleAdapter(this,remplir,R.layout.layoutlist,from,to)
        liste.setAdapter(adapter)



    }
    inner class Ecouteur : OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            val linearlayout = view as LinearLayout
            val textview = linearlayout.findViewById<TextView>(R.id.txtNom)
            Toast.makeText(this@KlaxonActivity,textview.text.toString(), Toast.LENGTH_SHORT).show()


        }

    }
}