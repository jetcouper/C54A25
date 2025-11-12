package com.example.annexe8b

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GaucheDroiteActivity : AppCompatActivity() {
    lateinit var gaucheDroite : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gauche_droite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var estClick = true

        gaucheDroite = findViewById(R.id.btnEffet)

        var anim = ObjectAnimator.ofFloat(gaucheDroite, View.TRANSLATION_X, 600f)

        gaucheDroite.setOnClickListener{
            if(!estClick){
                anim.reverse()
            }
            else{
                anim.start()
            }
            estClick = !estClick
        }

    }
}