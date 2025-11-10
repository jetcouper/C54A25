package com.example.annexe8

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var bouton : Button
    lateinit var element: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bouton = findViewById(R.id.button)
        element = findViewById(R.id.view)
        var anim = ObjectAnimator.ofFloat( element, View.X,  900f )
        anim.duration = 2000
        anim.interpolator = BounceInterpolator()

        var anim2 = ObjectAnimator.ofFloat(element, View.ROTATION_X, 360f )
        anim2.duration = 1000
        anim2.interpolator = BounceInterpolator()
        anim.repeatCount = ObjectAnimator.INFINITE
        anim.repeatMode = ObjectAnimator.REVERSE

        anim2.repeatCount = ObjectAnimator.INFINITE
        anim2.repeatMode = ObjectAnimator.REVERSE

        var animatorSet = AnimatorSet()
        animatorSet.playTogether(anim,anim2)
        bouton.setOnClickListener{
            animatorSet.start()

        }


    }
}