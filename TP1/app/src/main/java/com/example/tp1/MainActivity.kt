package com.example.tp1

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.example.tp1.ModeleChanson
import com.example.tp1.Sujet
import java.io.Serializable
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity(), ObservateurChangement {


    lateinit var liste : ListView
    lateinit var spinnerGenres : Spinner
    var listemusique = ArrayList<HashMap<String,Any>>()
    var lanceur : ActivityResultLauncher<Intent>? = null;
    lateinit var btnOption : Button
    lateinit var main : LinearLayout
    var volumeMusique : Int? = null
    var backgroundColor : String? = null

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
        spinnerGenres = findViewById(R.id.spinnerListeGenre)
        btnOption = findViewById(R.id.btnActiviteBoomerang)
        main = findViewById(R.id.main)
        val ec = Ecouteur()
        liste.onItemClickListener = ec

        spinnerGenres.onItemSelectedListener = ec

        btnOption.setOnClickListener{v:View -> lanceur?.launch(Intent(this@MainActivity,OptionsActivity::class.java))}


        lanceur = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackElement()
        )

    }
    inner class CallBackElement : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult) {
            var i = result.data
            if(result.resultCode == RESULT_OK){
                val couleurhex = i!!.getStringExtra("couleur")
                val volume = i!!.getIntExtra("volume", -1)
                main.setBackgroundColor(couleurhex!!.toColorInt())
                volumeMusique = volume
                backgroundColor = couleurhex

            }
        }


    }

    inner class Ecouteur : OnItemClickListener, AdapterView.OnItemSelectedListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            //val linearlayout = view as LinearLayout
            val item = listemusique
            val itemPos = position

            //val textview = linearlayout.findViewById<TextView>(R.id.txtTitle)
            val intent = Intent(this@MainActivity, LecteurActivity::class.java)
            intent.putExtra("musique", item as Serializable)
            intent.putExtra("position", itemPos)
            if(volumeMusique != null){
                intent.putExtra("volume", volumeMusique)
            }
            if(backgroundColor != null){
                intent.putExtra("background", backgroundColor)
            }
            startActivity(intent)
            //Toast.makeText(this@MainActivity,textview.text.toString(), Toast.LENGTH_SHORT).show()

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            val itemSelectionner = parent?.getItemAtPosition(position).toString()
            chargerListeParGenre(itemSelectionner)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            parent?.getItemAtPosition(0)
        }

    }
    fun chargerListeParGenre(item:String){
        var listemusiqueGenre = ArrayList<HashMap<String,Any>>()
        liste.adapter = null
        if(item == "Tout les genres"){
            listemusiqueGenre = ModeleChanson.retourListeMusique()
            val from = arrayOf("title","artist","duration","image","genre")
            val to = intArrayOf(R.id.txtTitle, R.id.txtArtiste,R.id.txtTemp,R.id.imageChanson, R.id.txtGenre)
            val adapter = SimpleAdapter(this,listemusiqueGenre,R.layout.layoutlistemusique,from,to)
            adapter.viewBinder = ImageUrlViewBinder()
            liste.adapter = adapter
        }
        if(item != "Tout les genres"){
            listemusiqueGenre = ModeleChanson.retourListeMusique()
            listemusiqueGenre = listemusiqueGenre.filter { it["genre"] == item } as ArrayList<HashMap<String, Any>>
            val from = arrayOf("title","artist","duration","image","genre")
            val to = intArrayOf(R.id.txtTitle, R.id.txtArtiste,R.id.txtTemp,R.id.imageChanson, R.id.txtGenre)
            val adapter = SimpleAdapter(this,listemusiqueGenre,R.layout.layoutlistemusique,from,to)
            adapter.viewBinder = ImageUrlViewBinder()
            liste.adapter = adapter
        }

    }


    fun remplirSpinner(spinner: Spinner){
        var listeGenres = ArrayList<String>()
        var hashmapChanson = ModeleChanson.retourListeMusique()
        listeGenres.add("Tout les genres")
        for (chanson in hashmapChanson){
            if(!listeGenres.contains(chanson["genre"])){
                listeGenres.add(chanson["genre"] as String)
            }
        }
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Default layout for spinner items
            listeGenres
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        ModeleChanson.init(applicationContext) //applicationContext pour éviter les memory leak
        ModeleChanson.ajouterObservateur(this)

    }


    override fun changement(nouvelleValeur: Int) {
        //C'est ici que l'on réagi aux changement, on met à jour la ListView
        listemusique = ModeleChanson.retourListeMusique()
        remplirSpinner(spinnerGenres)
        val from = arrayOf("title","artist","duration","image","genre")
        val to = intArrayOf(R.id.txtTitle, R.id.txtArtiste,R.id.txtTemp,R.id.imageChanson, R.id.txtGenre)
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