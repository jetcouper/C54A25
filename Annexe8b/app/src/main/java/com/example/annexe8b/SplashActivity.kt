package com.example.annexe8b

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var textSoleil : TextView
    lateinit var btnSoleil : Button
    lateinit var viewOrange : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnSoleil = findViewById(R.id.btnSoleil)
        textSoleil = findViewById(R.id.txtSoleil)
        viewOrange = findViewById(R.id.view)

        textSoleil.setZ(1f)

        val p: Path = Path()
        p.moveTo(445f,200f)
        p.lineTo(445f,1000f)
        p.lineTo(445f,800f)


        val anim = ObjectAnimator.ofFloat(viewOrange, View.X,View.Y,p)

        val aniAlpha = ObjectAnimator.ofFloat(textSoleil, View.ALPHA,1f)
        val animGrosseurX = ObjectAnimator.ofFloat(viewOrange, View.SCALE_X, 15f)
        val animGrosseurY = ObjectAnimator.ofFloat(viewOrange, View.SCALE_Y, 15f)
        val aniCouleur = ObjectAnimator.ofArgb(textSoleil,"textColor", Color.BLACK, Color.WHITE)
        val animeSet2 = AnimatorSet()
        animeSet2.duration = 1000


        val animeSetSequence = AnimatorSet()

        val animeSet = AnimatorSet()
        animeSet.duration = 1000



        animeSet.playTogether(anim,aniAlpha)

        animeSet2.playTogether(animGrosseurY,animGrosseurX,aniCouleur)

        animeSetSequence.playSequentially(animeSet,animeSet2)



        btnSoleil.setOnClickListener{
            animeSetSequence.start()

        }


    }

}