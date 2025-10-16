package com.example.tp1

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import androidx.media3.ui.TimeBar
import java.net.URL
import java.util.ArrayList
import kotlin.time.toDuration
import androidx.media3.common.util.UnstableApi
import kotlin.time.DurationUnit

@OptIn(UnstableApi::class)
class LecteurActivity : AppCompatActivity() {
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
    lateinit var barprogress: DefaultTimeBar

    lateinit var tempDepart: TextView
    lateinit var tempFin: TextView
    var hashMap : ArrayList<HashMap<String, Any>> ?= null
    var position : Long = 0
    lateinit var btnRetour: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lecteur)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        hashMap = ModeleChanson.listemusique //Appel du singleton
        position = intent!!.getIntExtra("position", 0).toLong()


        lecteur = findViewById(R.id.playerView)
        player = ExoPlayer.Builder(this).build()
        lecteur.player = player
        val playlist = ArrayList<MediaItem>()
        if (hashMap != null) {
            for (item in hashMap){
                val imageBytes = try {
                    URL(item["image"].toString()).readBytes()
                } catch (e: Exception) {
                    null
                }

                val metadata = MediaMetadata.Builder()
                    .setTitle(item["title"].toString())
                    .apply {
                        if (imageBytes != null)
                            setArtworkData(imageBytes, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
                    }
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(item["source"].toString())
                    .setMediaMetadata(metadata)
                    .build()

                playlist.add(mediaItem)
            }
        }
        player!!.setMediaItems(playlist)
        player!!.prepare()
        player!!.seekTo(position.toInt(),0)
        player!!.play()

        play = lecteur.findViewById(R.id.play)
        pause = lecteur.findViewById(R.id.pause)
        shuffle = lecteur.findViewById(R.id.melanger)
        repeat = lecteur.findViewById(R.id.repeter)
        nextMusic = lecteur.findViewById(R.id.suivant)
        preview = lecteur.findViewById(R.id.precedant)
        forward = lecteur.findViewById(R.id.avancer)
        playback = lecteur.findViewById(R.id.reculer)
        tempDepart = lecteur.findViewById(R.id.position_debut)
        tempFin = lecteur.findViewById(R.id.position_fin)
        btnRetour = findViewById(R.id.retour)
        barprogress = lecteur.findViewById(R.id.progression)

        val ec = Ecouteur()
        player!!.addListener(ec)

        play.setOnClickListener(ec)
        pause.setOnClickListener(ec)
        shuffle.setOnClickListener(ec)
        repeat.setOnClickListener(ec)
        nextMusic.setOnClickListener(ec)
        preview.setOnClickListener(ec)
        forward.setOnClickListener(ec)
        playback.setOnClickListener(ec)
        btnRetour.setOnClickListener(ec)


        barprogress.addListener(ec)


    }
    inner class Ecouteur: View.OnClickListener, Player.Listener, TimeBar.OnScrubListener{
        override fun onClick(v: View?) {
            when(v){
                play -> {
                    player!!.play()
                }
                pause -> {
                    player!!.pause()
                }
                shuffle -> {
                    player!!.shuffleModeEnabled = true
                }
                repeat -> player!!.repeatMode

                nextMusic -> {
                    player!!.seekToNextMediaItem()


                }
                preview -> {
                    player!!.seekToPreviousMediaItem()
                }
                forward -> {
                    var nouvellePosition = player!!.currentPosition + 10000
                    player!!.seekTo(nouvellePosition)

                }
                playback -> {
                    var nouvellePosition = (player!!.currentPosition - 10000).coerceAtLeast(0)
                    player!!.seekTo(nouvellePosition)

                }
                btnRetour -> {
                    finish()
                }

            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?,reason: Int)
        {
            super.onMediaItemTransition(mediaItem, reason)
            val duration = player?.duration ?: 0L
            val position = player?.currentPosition ?: 0L

            if (duration > 0) {
                barprogress.setDuration(duration)
                barprogress.setPosition(position)
            }

        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)



        }
        @UnstableApi
        override fun onScrubStart(timeBar: TimeBar, position: Long) {
            player!!.pause()
        }
        @UnstableApi
        override fun onScrubMove(timeBar: TimeBar, position: Long) {
            tempDepart.text = position.toDuration(DurationUnit.MILLISECONDS).toString()

        }
        @UnstableApi
        override fun onScrubStop(timeBar: TimeBar,position: Long,canceled: Boolean) {
            if (!canceled) {
                player!!.seekTo(position)
                player!!.play()
            }
        }

    }


    override fun onStart() {
        super.onStart()
    }
}




