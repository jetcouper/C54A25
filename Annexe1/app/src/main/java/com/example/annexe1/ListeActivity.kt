package com.example.annexe1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader

class ListeActivity : AppCompatActivity() {
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

        listeMemo.setAdapter(ArrayAdapter(this,android.R.layout.simple_list_item_1, lireMemos()))
        //listeMemo.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, lireMemos());

    }

    fun lireMemos():ArrayList<String>{
        //Flux de données
        val ofi = openFileInput("fichier.txt")
        val isr = InputStreamReader(ofi)
        val br = BufferedReader(isr)

        val arrayliste = ArrayList<String>()

        //Version du prof
//        var ligne = br.readLine()
//        while (ligne != null){
//            arrayliste.add(ligne)
//            ligne = br.readLine()
//        }
//        br.close()

        //Ma version
        for(line in br.lines()){
            arrayliste.add(line)
        }

        br.close()
        return arrayliste
    }

}