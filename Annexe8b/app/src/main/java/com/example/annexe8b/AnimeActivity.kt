package com.example.annexe8b

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AnimeActivity : AppCompatActivity() {
    lateinit var texte : TextView
    lateinit var btnSplash : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_anime)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        texte = findViewById(R.id.txtAnime)
        btnSplash = findViewById(R.id.btnStartAnime)


        var animX = ObjectAnimator.ofFloat(texte, View.SCALE_X, 3f)
        var animY = ObjectAnimator.ofFloat(texte, View.SCALE_Y, 3f)

        val aniAlpha = ObjectAnimator.ofFloat(texte, View.ALPHA,1f)

        val animeSet = AnimatorSet()
        animeSet.duration = 1000 //Par défaut 300 ms
        animeSet.interpolator = BounceInterpolator()
        animeSet.playTogether(animX,animY,aniAlpha)

        btnSplash.setOnClickListener{

            animeSet.start()
        }
    }
}