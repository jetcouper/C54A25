package com.example.annexe4

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RepondreActivity : AppCompatActivity() {

    lateinit var boutonConfirmer: Button
    lateinit var texteNom: EditText
    lateinit var textePrenom:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_repondre)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        boutonConfirmer = findViewById(R.id.btnConfirmer)
        texteNom = findViewById(R.id.txtNom)
        textePrenom = findViewById(R.id.txtPrenom)




        boutonConfirmer.setOnClickListener {
            var i = Intent() //De retour
            var user = Utilisateur(texteNom.text.toString(),textePrenom.text.toString())
            i.putExtra("user",user)
            setResult(RESULT_OK,i)
            finish() //Superposées . Visuellement je revient à MainActivity
        }


    }
}