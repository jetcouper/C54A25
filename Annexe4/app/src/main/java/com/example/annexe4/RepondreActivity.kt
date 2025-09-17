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



        val ec = Ecouteur()
        boutonConfirmer.setOnClickListener(ec)


    }
    inner class Ecouteur : View.OnClickListener {
        override fun onClick(v: View?) {
            var user = Utilisateur(texteNom.text.toString(),textePrenom.text.toString())
            var i = Intent(this@RepondreActivity,MainActivity::class.java)
            i.putExtra("user",user)
            setResult(RESULT_OK,i)
            finish()
        }

    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}