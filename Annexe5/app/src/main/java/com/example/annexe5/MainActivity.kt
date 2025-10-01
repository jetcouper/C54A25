package com.example.annexe5

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var listeMusique:ListView
    lateinit var adapter: SimpleAdapter
    var v:ArrayList<HashMap<String,Any>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ec = Ecouteur()


        listeMusique = findViewById(R.id.listeChanson)
        v = remplirArrayListe()
        val from = arrayOf("No", "Title", "Date", "Image")
        val to = intArrayOf(R.id.noChanson, R.id.titreChanson,R.id.dateChanson,R.id.imageChanson) // IDs of views in your list_item_layout.xml

        adapter = SimpleAdapter(this, v, R.layout.layoutlist, from, to)
        listeMusique.setAdapter(adapter)

        //listeMusique.setOnItemClickListener(ec) //Java style
        listeMusique.onItemClickListener = ec //Kotlin style


        //Version lambda
//        listeMusique.setOnItemClickListener{_, view , _, _ ->
//            val parent = view as LinearLayout
//            val chamNom = parent.findViewById<TextView>(R.id.titreChanson)
//            Toast.makeText(
//                this@MainActivity,
//                chamNom.text.toString(),
//                Toast.LENGTH_SHORT
//            ).show()
//
//        }

    }

    inner class Ecouteur : OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            //On part de position
//            val nomChanson = v[position]["Title"] as String
//
//            Toast.makeText(this@MainActivity,nomChanson, Toast.LENGTH_SHORT).show()

            //On part du paramètre View
            val linearlayout = view as LinearLayout
            val textview = linearlayout.findViewById<TextView>(R.id.titreChanson)
            Toast.makeText(this@MainActivity,textview.text.toString(), Toast.LENGTH_SHORT).show()


        }

    }

    fun remplirArrayListe() : ArrayList<HashMap<String, Any>>{
        val dataList = ArrayList<HashMap<String, Any>>()
        var itemMap: HashMap<String, Any>

        itemMap = HashMap()
        //itemMap.put("No",3) //Autre façon
        itemMap["No"] = "3"
        itemMap["Title"] = "Touch Me"
        itemMap["Date"] = "22/03/1986"
        itemMap["Image"] = R.drawable.touchme
        dataList.add(itemMap)

        itemMap = HashMap()
        itemMap["No"] = "8"
        itemMap["Title"] = "Nothing's gonna stop me now"
        itemMap["Date"] = "30/05/1986"
        itemMap["Image"] = R.drawable.nothing
        dataList.add(itemMap)

        itemMap = HashMap()
        itemMap["No"] = "31"
        itemMap["Title"] = "Santa Maria"
        itemMap["Date"] = "28/03/1998"
        itemMap["Image"] = R.drawable.santamaria
        dataList.add(itemMap)

        itemMap = HashMap()
        itemMap["No"] = "108"
        itemMap["Title"] = "Hot boy"
        itemMap["Date"] = "10/04/2018"
        itemMap["Image"] = R.drawable.hotboy
        dataList.add(itemMap)

        return dataList
    }




}