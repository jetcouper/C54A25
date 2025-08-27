package com.example.annexe1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat




class AjouterActivity : AppCompatActivity() {

    lateinit var boutonAjouter: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajouter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            boutonAjouter = findViewById(R.id.btnAjouterMemo);

            val ec = Ecouteur();
            boutonAjouter.setOnClickListener(ec)


            insets
        }
    }

    private inner class Ecouteur : View.OnClickListener {
        override fun onClick(v: View?) {
            

        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}