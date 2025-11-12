package com.example.annexe8b

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var gaucheDroite : Button
    lateinit var animeTitre: Button
    lateinit var splash:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        gaucheDroite = findViewById(R.id.btnGaucheDroite)
        animeTitre = findViewById(R.id.btnAnimeTitre)
        splash = findViewById(R.id.btnSplash)


        gaucheDroite.setOnClickListener{
            var intent = Intent(this, GaucheDroiteActivity::class.java)
            startActivity(intent)

        }
        animeTitre.setOnClickListener{
            var intent = Intent(this, AnimeActivity::class.java)
            startActivity(intent)
        }

        splash.setOnClickListener{
            var intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)

        }


    }
}