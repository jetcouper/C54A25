package com.example.tp1

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.util.ArrayList

class LecteurActivity : AppCompatActivity() , ObservateurChangement {
    lateinit var lecteur: PlayerView
    var player : ExoPlayer? = null
    lateinit var play: ImageButton
    lateinit var pause: ImageButton
    lateinit var shuffle: ImageButton
    lateinit var repeat: ImageButton
    lateinit var nextMusic: ImageButton
    lateinit var preview: ImageButton
    lateinit var forward: ImageButton
    lateinit var playback: ImageButton
    lateinit var hashMap : ArrayList<HashMap<String, Any>>
    var position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lecteur)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //play = findViewById(R.layout.exo_player_layout.)
        hashMap = (intent.getSerializableExtra("musique") as? ArrayList<HashMap<String, Any>>)!!

        lecteur = findViewById(R.id.playerView)
        player = ExoPlayer.Builder(this).build()
        lecteur.player = player
        //lecteur.useController = true

        play = lecteur.findViewById(R.id.play)
        pause = lecteur.findViewById(R.id.pause)
        shuffle = lecteur.findViewById(R.id.melanger)
        repeat = lecteur.findViewById(R.id.repeter)
        nextMusic = lecteur.findViewById(R.id.suivant)
        preview = lecteur.findViewById(R.id.precedant)
        forward = lecteur.findViewById(R.id.avancer)
        playback = lecteur.findViewById(R.id.reculer)

        val ec = Ecouteur()

        play.setOnClickListener(ec)
        pause.setOnClickListener(ec)
        shuffle.setOnClickListener(ec)
        repeat.setOnClickListener(ec)
        nextMusic.setOnClickListener(ec)
        preview.setOnClickListener(ec)
        forward.setOnClickListener(ec)
        playback.setOnClickListener(ec)



    }
    inner class Ecouteur: View.OnClickListener{
        override fun onClick(v: View?) {

            when(v){
                play -> {
                    player!!.play()
                }
                pause -> {
                    player!!.pause()
                }
                shuffle -> {
                    player!!.shuffleModeEnabled

                }
                repeat -> ""
                nextMusic -> {
                    player!!.seekToNext()
                }
                preview -> {
                    player!!.seekBack()
                }
                forward -> {
                    var positionCourante = player!!.currentPosition
                    var nouvellePosition = positionCourante + 1000
                    player!!.seekTo(nouvellePosition)
                }
                playback -> {
                    var positionCourante = player!!.currentPosition
                    var nouvellePosition = positionCourante - 1000
                    if(nouvellePosition < 0){
                        nouvellePosition = 0
                        player!!.seekTo(nouvellePosition)
                    }

                }

            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun changement(nouvelleValeur: Int) {
        TODO("Not yet implemented")
    }
}