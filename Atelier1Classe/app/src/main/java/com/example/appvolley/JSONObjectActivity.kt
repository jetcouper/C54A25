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
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class JSONObjectActivity : AppCompatActivity() {
    val url = "https://api.jsonbin.io/v3/b/67fe6a908a456b796689f63d?meta=false"
    lateinit var liste : ListView
    var v:ArrayList<HashMap<String,Any>> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jsonobject)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        liste = findViewById(R.id.listProduit)
        val queue = Volley.newRequestQueue(this)



        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val tab = response.getJSONArray("accessoires")
                decomposerReponse(tab)
            },
            { error ->
                Toast.makeText(this@JSONObjectActivity,"Erreur ne fonctionne pas.", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(jsonObjectRequest)


        val ec = Ecouteur()

        liste.onItemClickListener = ec
    }


    fun decomposerReponse(tab:JSONArray)
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
            Toast.makeText(this@JSONObjectActivity,textview.text.toString(), Toast.LENGTH_SHORT).show()


        }

    }



}





























