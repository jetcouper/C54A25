package com.example.annexe1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException

class AfficherActivity : AppCompatActivity() {
    lateinit var listeMemo: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_liste)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listeMemo = findViewById(R.id.lstMemo)

        listeMemo.setAdapter(ArrayAdapter(this,android.R.layout.simple_list_item_1, lireMemos()!!))
        //listeMemo.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, lireMemos());

    }

    fun lireMemos():ArrayList<String>?{

        var liste: ArrayList<Memo>? = null
        var trier:ArrayList<String>? = null
        try {
            liste = SingletonMemos.recupererListe(this@AfficherActivity) //Liste de memos qui vient du singleton
            liste.sortWith(compareBy { it.echeance }) // Trier en fonction de l'échéance
            trier = ArrayList<String>() //Liste de strings vide
            for (memo in liste){ //pr chaque memo dans la liste
                trier.add(memo.message + " - " + memo.echeance.toString()) //Ajoute les messages de chaque mémo
            }

        }catch (f:FileNotFoundException){
            Toast.makeText(this@AfficherActivity," pas de fichier de serialisation",Toast.LENGTH_LONG).show()
            finish()
        }



        return trier
    }

}