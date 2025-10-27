package com.example.tp1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
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
import kotlin.time.Duration
import kotlin.time.DurationUnit


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
    lateinit var barprogress: SeekBar
    lateinit var btnLien : Button

    lateinit var tempDepart: TextView
    lateinit var tempFin: TextView
    var hashMap : ArrayList<HashMap<String, Any>> ?= null
    var position : Long = 0
    lateinit var btnRetour: Button
    var timer : CountDownTimer? = null
    var tempSauvegarder : Long? = null


    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
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
        //lecteur.setUseController(false);
        lecteur.player = player
        val playlist = ArrayList<MediaItem>()
        if (hashMap != null) {
            for (item in hashMap!!){
                val durationString = item["duration"] as String
                var parties = durationString.split(":")

                val minute = parties[0].toLongOrNull()
                val seconde = parties[1].toLongOrNull()

                val duration = seconde?.let {
                    minute?.toDuration(DurationUnit.MINUTES)
                        ?.plus(it.toDuration(DurationUnit.SECONDS))
                }
                val millisecondes = duration?.toLong(DurationUnit.MILLISECONDS)
                val metadata = MediaMetadata.Builder()
                    .setTitle(item["title"].toString())
                    .setDurationMs(millisecondes)
                    .setComposer(item["site"].toString())
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
        Toast.makeText(this, player!!.mediaMetadata.title.toString(), Toast.LENGTH_SHORT).show()
        timer = MonTimer(player!!.mediaMetadata.durationMs as Long, 1000)
        timer?.start()
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
        btnLien = findViewById(R.id.lienChanson)

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
        btnLien.setOnClickListener(ec)

        barprogress.setOnSeekBarChangeListener(ec)
        barprogress.max = (player!!.mediaMetadata.durationMs?.toInt()!!)
        //barprogress.top = player!!.currentPosition.toInt()



    }
    inner class Ecouteur: View.OnClickListener, Player.Listener, SeekBar.OnSeekBarChangeListener{
        @OptIn(UnstableApi::class) override fun onClick(v: View?) {
            when(v){
                play -> {
                    player!!.play()
                    timer?.start()


                }
                pause -> {

                    player!!.pause()
                    timer?.cancel()

                }
                shuffle -> {
                    player!!.shuffleModeEnabled = true
                }
                repeat -> {
                    player!!.repeatMode
                }

                nextMusic -> {
                    if(player!!.hasNextMediaItem()){
                        player!!.seekToNextMediaItem()
                        barprogress.max = (player!!.mediaMetadata.durationMs?.toInt()!!)
                        timer = MonTimer(player!!.mediaMetadata.durationMs as Long, 1000)
                        Toast.makeText(this@LecteurActivity, player!!.mediaMetadata.title.toString(), Toast.LENGTH_SHORT).show()
                    }
                    if(!player!!.hasNextMediaItem()){
                        player!!.seekTo(0,0)
                        barprogress.max = (player!!.mediaMetadata.durationMs?.toInt()!!)
                        timer = MonTimer(player!!.mediaMetadata.durationMs as Long, 1000)
                        Toast.makeText(this@LecteurActivity, player!!.mediaMetadata.title.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
                preview -> {
                    if(player!!.hasPreviousMediaItem()){
                        player!!.seekToPreviousMediaItem()
                        barprogress.max = (player!!.mediaMetadata.durationMs?.toInt()!!)
                        timer = MonTimer(player!!.mediaMetadata.durationMs as Long, 1000)
                        Toast.makeText(this@LecteurActivity, player!!.mediaMetadata.title.toString(), Toast.LENGTH_SHORT).show()
                    }
                    if(!player!!.hasPreviousMediaItem()){
                        player!!.seekTo(0,0)
                        barprogress.max = (player!!.mediaMetadata.durationMs?.toInt()!!)
                        timer = MonTimer(player!!.mediaMetadata.durationMs as Long, 1000)
                        Toast.makeText(this@LecteurActivity, player!!.mediaMetadata.title.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
                forward -> {
                    //10 seconde de plus
                    var nouvellePosition = player!!.currentPosition + 10000
                    player!!.seekTo(nouvellePosition)

                }
                playback -> {
                    //10 seconde de moins
                    var nouvellePosition = (player!!.currentPosition - 10000).coerceAtLeast(0)
                    player!!.seekTo(nouvellePosition)

                }
                btnRetour -> {
                    finish()
                }
                btnLien -> {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse((player!!.mediaMetadata.composer).toString()))
                    startActivity(i)
                }

            }
        }

        override fun onProgressChanged(seekBar: SeekBar?,progress: Int,fromUser: Boolean) {

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }


    }

    inner class MonTimer(duration: Long, intervale : Long): CountDownTimer(duration,intervale)
    {
        private var millisDuration : Long = duration
        override fun onTick(millisUntilFinished: Long) {
            val elapsed = millisDuration - millisUntilFinished
            tempDepart.text = DateUtils.formatElapsedTime(elapsed / 1000)
            tempFin.text = DateUtils.formatElapsedTime(millisDuration / 1000)
            barprogress.progress = elapsed.toInt()
        }

        override fun onFinish() {
            tempFin.text = ""
            tempDepart.text = ""
        }

    }

    override fun onStop() {
        super.onStop()
        player!!.release()
        player = null
    }

    override fun onStart() {
        super.onStart()
    }
}




