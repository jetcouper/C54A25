package com.example.tp1

import EtatApplication
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
import android.view.ViewGroup
import androidx.core.content.ContextCompat




class MainActivity : AppCompatActivity(), ObservateurChangement {


    lateinit var liste : ListView
    lateinit var spinnerGenres : Spinner
    lateinit var btnOption : Button
    lateinit var main : LinearLayout

    var listemusique = ArrayList<HashMap<String,Any>>()
    var lanceur : ActivityResultLauncher<Intent>? = null;
    var volumeMusique : Int? = null
    var backgroundColor : String? = null
    var position : Int? = null
    var listemusiqueGenre : ArrayList<HashMap<String, Any>>? = null
    var genre : String? = null

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
        liste.setBackgroundResource(R.drawable.effet_list_view_frame)
        spinnerGenres = findViewById(R.id.spinnerListeGenre)
        btnOption = findViewById(R.id.btnActiviteBoomerang)
        main = findViewById(R.id.main)

        val etat = SerialisationUtil.restaurerEtat(this)

        if (etat != null) {
            // Si l'activité sauvegardée n'est pas MainActivity, on la lance au-dessus
            if (etat.activiteCourante != this::class.java.name) {
                val intent = Intent(this, Class.forName(etat.activiteCourante))

                // Remettre les extras sauvegardés
                for ((key, value) in etat.extraIntent) {
                    when (value) {
                        is String -> intent.putExtra(key, value)
                        is Int -> intent.putExtra(key, value)
                        is Float -> intent.putExtra(key, value)
                        is Boolean -> intent.putExtra(key, value)
                        is Serializable -> intent.putExtra(key, value)
                    }
                }

                // Évite de créer plusieurs instances si déjà ouverte
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            } else {
                // Sinon, restore des valeurs pour MainActivity
                volumeMusique = etat.extraIntent["volume"] as? Int
                backgroundColor = etat.extraIntent["background"] as? String
                position = etat.extraIntent["position"] as? Int
                listemusiqueGenre = etat.extraIntent["listemusiqueGenre"] as? ArrayList<HashMap<String, Any>>
                genre = etat.extraIntent["genre"] as? String
                backgroundColor?.let { main.setBackgroundColor(it.toColorInt()) }
            }
        }



        // Aucune sauvegarde → lancement normal
        val ec = Ecouteur()
        liste.onItemClickListener = ec
        spinnerGenres.onItemSelectedListener = ec
        btnOption.setOnClickListener{v:View ->
            val intent = Intent(this@MainActivity,OptionsActivity::class.java)
            intent.putExtra("volume", volumeMusique)
            intent.putExtra("couleur", backgroundColor)
            lanceur?.launch(intent)
        }
        lanceur = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackElement()
        )
    }

    //Le retour de l'activité options avec ces résultats
    inner class CallBackElement : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult) {
            var i = result.data
            if(result.resultCode == RESULT_OK){
                val couleurhex = i!!.getStringExtra("couleur")
                val volume = i!!.getIntExtra("volume", -1)
                couleurhex?.let { main.setBackgroundColor(it.toColorInt()) }
                volumeMusique = volume
                backgroundColor = couleurhex


                listemusiqueGenre?.let { chargerListeParGenre(genre ?: "Tout les genres") }
                position?.let { liste.setSelection(it) }

            }
        }


    }


    inner class Ecouteur : OnItemClickListener, AdapterView.OnItemSelectedListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, positionItem: Int, id: Long) {

            val genreChoisi = spinnerGenres.selectedItem.toString()
            listemusiqueGenre = ModeleChanson.retourListeMusique()



            if(spinnerGenres.selectedItem != "Tout les genres"){
                listemusiqueGenre = listemusiqueGenre?.filter {
                    it["genre"] == genreChoisi
                } as ArrayList<HashMap<String, Any>>
            }
            val item = listemusiqueGenre
            position = positionItem


            val intent = Intent(this@MainActivity, LecteurActivity::class.java)
            intent.putExtra("musique", item as Serializable)
            intent.putExtra("position", position)
            if(volumeMusique != null){
                intent.putExtra("volume", volumeMusique)
            }
            if(backgroundColor != null){
                intent.putExtra("background", backgroundColor)
            }
            startActivity(intent)

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            val itemSelectionner = parent?.getItemAtPosition(position).toString()
            chargerListeParGenre(itemSelectionner)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            parent?.getItemAtPosition(0)
        }

    }

    //Dépendament de ce qui est choisi dans le spinner, la listview va charger la liste choisi
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
        genre = item

    }

    //Remplir le spinner de mes genres de musique. (Éviter les doublons)
    fun remplirSpinner(spinner: Spinner, selection: String? = null){
        var listeGenres = ArrayList<String>()
        var hashmapChanson = ModeleChanson.retourListeMusique()
        listeGenres.add("Tout les genres")
        for (chanson in hashmapChanson){
            if(!listeGenres.contains(chanson["genre"])){
                listeGenres.add(chanson["genre"] as String)
            }
        }
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            listeGenres
        )
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = super.getView(position, convertView, parent) as TextView
                tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                return tv
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = super.getDropDownView(position, convertView, parent) as TextView
                tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                tv.setBackgroundResource(R.drawable.effet_spinner_items)
                return tv
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        //Dépendament du genre choisi en paramètre, il va sélectionner le genre dans le spinner
        selection?.let {
            val index = listeGenres.indexOf(it)
            if (index >= 0) {
                spinner.setSelection(index)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        ModeleChanson.init(applicationContext) //applicationContext pour éviter les memory leak
        ModeleChanson.ajouterObservateur(this)

    }

    override fun onPause() {
        super.onPause()
        val extras = HashMap<String, Any>()
        volumeMusique?.let { extras["volume"] = it }
        backgroundColor?.let { extras["background"] = it }
        position?.let { extras["position"] = it }
        listemusiqueGenre?.let { extras["listemusiqueGenre"] = it }
        genre?.let { extras["genre"] = it }


        val etat = EtatApplication(
            activiteCourante = this::class.java.name,
            extraIntent = extras
        )

        SerialisationUtil.sauvegarderEtat(this, etat)
    }

    override fun changement(nouvelleValeur: Int) {
        //C'est ici que l'on réagi aux changement, on met à jour la ListView
        listemusique = ModeleChanson.retourListeMusique()
        remplirSpinner(spinnerGenres, genre)
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