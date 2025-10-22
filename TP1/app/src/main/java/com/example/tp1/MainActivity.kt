package com.example.tp1

import android.app.Application
import android.content.Intent
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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.example.tp1.ModeleChanson
import com.example.tp1.Sujet
import java.io.Serializable

class MainActivity : AppCompatActivity(), ObservateurChangement {


    lateinit var liste : ListView
    var listemusique = ArrayList<HashMap<String,Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        liste = findViewById(R.id.listPlaylist)
        val ec = Ecouteur()
        liste.onItemClickListener = ec





    }

    inner class Ecouteur : OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            //val linearlayout = view as LinearLayout
            val item = listemusique
            val itemPos = position

            //val textview = linearlayout.findViewById<TextView>(R.id.txtTitle)
            val intent = Intent(this@MainActivity, LecteurActivity::class.java)
            intent.putExtra("musique", item as Serializable)
            intent.putExtra("position", itemPos)
            startActivity(intent)
            //Toast.makeText(this@MainActivity,textview.text.toString(), Toast.LENGTH_SHORT).show()

        }

    }

    override fun onStart() {
        super.onStart()
        ModeleChanson.init(applicationContext) //applicationContext pour éviter les memory leak
        ModeleChanson.ajouterObservateur(this)

    }

    override fun changement(nouvelleValeur: Int) {
        //C'est ici que l'on réagi aux changement, on met à jour la ListView
        listemusique = ModeleChanson.retourListeMusique()
        val from = arrayOf("title","artist","duration","image")
        val to = intArrayOf(R.id.txtTitle, R.id.txtArtiste,R.id.txtTemp,R.id.imageChanson)
        val adapter = SimpleAdapter(this,listemusique,R.layout.layoutlistemusique,from,to)
        adapter.viewBinder = ImageUrlViewBinder()

        liste.adapter = adapter
    }



    inner class ImageUrlViewBinder() : SimpleAdapter.ViewBinder{
        override fun setViewValue(view: View?,data: Any?,textRepresentation: String?): Boolean {
            if(view is ImageView && data is String){
                Glide.with(this@MainActivity).load(data).into(view)
                return true
            }
            return false
        }
    }


}