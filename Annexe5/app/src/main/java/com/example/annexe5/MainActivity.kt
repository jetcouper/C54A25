package com.example.annexe5

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var listeMusique:ListView
    lateinit var adapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listeMusique = findViewById(R.id.listeChanson)




        val from = arrayOf("No", "Title", "Date", "Image")
        val to = intArrayOf(R.id.noChanson, R.id.titreChanson,R.id.dateChanson,R.id.imageChanson) // IDs of views in your list_item_layout.xml

        adapter = SimpleAdapter(this, remplirArrayListe(), R.layout.layoutlist, from, to)
        listeMusique.setAdapter(adapter)

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