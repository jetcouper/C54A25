package com.example.annexe8

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class exercice1 : AppCompatActivity() {
    lateinit var layout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercice1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var estStart = true
        layout = findViewById(R.id.layoutImage)
        var anim = ObjectAnimator.ofFloat( layout, View.TRANSLATION_Y,  40f )


        layout.setOnClickListener{
            if(!estStart){
                anim.reverse()
            }
            else{
                anim.start()
            }
            estStart = !estStart
        }
    }
}