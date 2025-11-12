package com.antoine.examen

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var reponse : TextView
    lateinit var progress : ProgressBar
    lateinit var verifier : Button
    lateinit var album : EditText

    var listeChanson : ArrayList<HashMap<String, Any>>? = null
    var trouver: Boolean = false
    var iteration : Int = 0
    var listNommer : ArrayList<String>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        reponse = findViewById(R.id.champReponse)
        progress = findViewById(R.id.progressBar)
        verifier = findViewById(R.id.boutonVerifier)
        album = findViewById(R.id.champAlbum)
        //listeChanson = JSONObjectActivity.retourListeMusique()
        listNommer = ArrayList<String>()
        val ec = Ecouteur()

        verifier.setOnClickListener(ec)


    }

    inner class Ecouteur : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v){
                verifier -> {
                    listeChanson = JSONObjectActivity.retourListeMusique()
                    trouver = false
                    if(!album.text.isNullOrEmpty()){
                        if(!listNommer!!.contains(album.text.toString())){
                            for(item in listeChanson!!){

                                if(item.get("nom") == album.text.toString()){
                                    trouver = true
                                    iteration += 1
                                    progress.progress = iteration
                                    var text = album.text.toString()
                                    listNommer!!.add(text)
                                    Toast.makeText(this@MainActivity,"Chanson ${album.text.toString()} trouvé!", Toast.LENGTH_LONG).show()
                                    album.text.clear()
                                    if(progress.progress == 3){
                                        Toast.makeText(this@MainActivity,"Vous avez trouvé trois chanson de l'artiste Bĩa", Toast.LENGTH_LONG).show()
                                    }
                                    break
                                }
                                else{

                                    trouver = false
                                }

                            }
                        }
                        if(trouver == false && !listNommer!!.contains(album.text.toString())){
                            Toast.makeText(this@MainActivity,"Mauvaise réponse", Toast.LENGTH_LONG).show()
                        }
                        if(listNommer!!.contains(album.text.toString()))
                        {
                            Toast.makeText(this@MainActivity,"${album.text.toString()} à déjà été donné.", Toast.LENGTH_LONG).show()
                        }

                    }
                    else{
                        Toast.makeText(this@MainActivity,"Veuillez inscrire quelque chose", Toast.LENGTH_LONG).show()
                    }
                    album.text.clear()
                }


            }
        }


    }

    override fun onStart() {
        super.onStart()
        JSONObjectActivity.init(applicationContext)
        listeChanson = JSONObjectActivity.retourListeMusique()
    }
}