package com.example.tp1

import EtatApplication
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.util.ArrayList
import androidx.media3.common.util.UnstableApi


class LecteurActivity : AppCompatActivity() {
    lateinit var lecteur: PlayerView
    lateinit var play: ImageButton
    lateinit var pause: ImageButton
    lateinit var nextMusic: ImageButton
    lateinit var preview: ImageButton
    lateinit var forward: ImageButton
    lateinit var playback: ImageButton
    lateinit var barprogress: SeekBar
    lateinit var btnLien : Button
    lateinit var txtNom : TextView
    lateinit var txtAlbum : TextView
    lateinit var txtArtiste : TextView
    lateinit var txtGenre : TextView
    lateinit var main : LinearLayout
    lateinit var btnRetour: Button
    lateinit var tempDepart: TextView
    lateinit var tempFin: TextView

    var player : ExoPlayer? = null
    var hashMap : ArrayList<HashMap<String, Any>> ?= null
    var timer : CountDownTimer? = null
    var volumeMusique: Int? = null
    var backgroundColor: String? = null
    var positionIndex : Int = 0
    var positionMs : Long = 0L
    var isPlaying: Boolean = true
    var restorerUneFois = false



    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lecteur)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lecteur = findViewById(R.id.playerView)
        txtNom = findViewById(R.id.txtNomLecteur)
        txtAlbum = findViewById(R.id.txtAlbumLecteur)
        txtGenre = findViewById(R.id.txtGenreLecteur)
        txtArtiste = findViewById(R.id.txtArtisteLecteur)
        play = lecteur.findViewById(R.id.play)
        pause = lecteur.findViewById(R.id.pause)
        nextMusic = lecteur.findViewById(R.id.suivant)
        preview = lecteur.findViewById(R.id.precedant)
        forward = lecteur.findViewById(R.id.avancer)
        playback = lecteur.findViewById(R.id.reculer)
        tempDepart = lecteur.findViewById(R.id.position_debut)
        tempFin = lecteur.findViewById(R.id.position_fin)
        btnRetour = findViewById(R.id.retour)
        barprogress = lecteur.findViewById(R.id.progression)
        btnLien = findViewById(R.id.lienChanson)
        main = findViewById(R.id.main)
        val ec = Ecouteur()



        //Restauration du dernier événement avec ces composantes
        val etat = SerialisationUtil.restaurerEtat(this)
        if (etat!!.activiteCourante == this::class.java.name) {
            hashMap = etat.extraIntent["musique"] as? ArrayList<HashMap<String, Any>>
            positionIndex = etat.extraIntent["currentIndex"] as? Int ?: 0
            positionMs = etat.extraIntent["currentPosition"] as? Long ?: 0L
            volumeMusique = etat.extraIntent["volume"] as? Int ?: 100
            isPlaying = etat.extraIntent["isPlaying"] as? Boolean ?: true
            backgroundColor = etat.extraIntent["background"] as? String
        } else {
            hashMap = intent.getSerializableExtra("musique") as? ArrayList<HashMap<String, Any>>
            positionIndex = intent.getIntExtra("position", 0)
            positionMs = 0L
            volumeMusique = intent.getIntExtra("volume", 100)
            isPlaying = true
            backgroundColor = intent.getStringExtra("background")
        }

        // Couleur de fond
        backgroundColor?.let { main.setBackgroundColor(it.toColorInt()) }

        //Initialisation du player avec son volume
        player = ExoPlayer.Builder(this).build()
        lecteur.player = player
        player?.volume = (volumeMusique?.toFloat() ?: 100f) / 100f



        //Création d'une playlist avec les métaDonnées
        val playlist = ArrayList<MediaItem>()
        if (hashMap != null) {
            for (item in hashMap!!){

                val metadata = MediaMetadata.Builder()
                    .setTitle(item["title"].toString())
                    .setComposer(item["site"].toString())
                    .setGenre(item["genre"].toString())
                    .setArtist(item["artist"].toString())
                    .setAlbumTitle(item["album"].toString())
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(item["source"].toString())
                    .setMediaMetadata(metadata)
                    .build()

                playlist.add(mediaItem)
            }
        }
        player!!.setMediaItems(playlist)
        player!!.addListener(ec)
        player!!.prepare()

        player!!.seekTo(positionIndex, positionMs)
        player!!.playWhenReady = isPlaying

        //Initialisation des écouteur
        play.setOnClickListener(ec)
        pause.setOnClickListener(ec)
        nextMusic.setOnClickListener(ec)
        preview.setOnClickListener(ec)
        forward.setOnClickListener(ec)
        playback.setOnClickListener(ec)
        btnRetour.setOnClickListener(ec)
        btnLien.setOnClickListener(ec)
        barprogress.setOnSeekBarChangeListener(ec)



    }
    inner class Ecouteur: View.OnClickListener, Player.Listener, SeekBar.OnSeekBarChangeListener{
        @OptIn(UnstableApi::class) override fun onClick(v: View?) {
            when(v){
                play -> {
                    player?.playWhenReady = true
                    player!!.play()
                    timer?.start()
                }
                pause -> {
                    player!!.pause()
                    timer?.cancel()
                }

                nextMusic -> {
                    if(player!!.hasNextMediaItem()){
                        player!!.seekToNextMediaItem()
                        player!!.playWhenReady = true
                    }
                    //S'il n'y a pas de prochaine musique, retourner au début de la playlist.
                    else{
                        player!!.seekTo(0,0)
                        player!!.playWhenReady = true
                    }

                }
                preview -> {
                    if(player!!.hasPreviousMediaItem()){
                        player!!.seekToPreviousMediaItem()
                        player!!.playWhenReady = true
                    }
                    if(!player!!.hasPreviousMediaItem()){
                        player!!.seekTo(0,0)
                        player!!.playWhenReady = true
                    }

                }
                forward -> {
                    //10 seconde de plus
                    var nouvellePosition = (player!!.currentPosition + 10000).coerceAtMost(player!!.duration)
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
                    //Lien vers internet
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse((player!!.mediaMetadata.composer).toString()))
                    startActivity(i)
                }

            }
        }

        //Si je veux changer la position de la musique en cours
        override fun onProgressChanged(seekBar: SeekBar?,progress: Int,fromUser: Boolean) {
            if(fromUser){
                player!!.pause()
                seekBar?.progress = progress
                player!!.seekTo(seekBar!!.progress.toLong())
                player!!.play()
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

        //Changement de l'affichage à chaque fois que la médiaData(Musique) change
        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
            if (player != null) {
                changerText(player!!)
                val dur = player?.duration ?: 0L
                if (dur > 0) {
                    barprogress.max = dur.toInt()
                    timer?.cancel()
                    timer = MonTimer(dur, 1000)
                    timer?.start()
                }
            }
        }

        //Si la mediaData est charger depuis une sérialisation(Plus stable).
        @OptIn(UnstableApi::class) override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_READY && !restorerUneFois) {
                restorerUneFois = true
                val idx = positionIndex.coerceAtLeast(0).coerceAtMost((player?.mediaItemCount ?: 1) - 1)
                val pos = positionMs.coerceAtLeast(0L)
                player?.seekTo(idx, pos)
                player?.playWhenReady = isPlaying
                changerText(player!!)
                val dur = player?.duration ?: 0L
                if (dur > 0) {
                    barprogress.max = dur.toInt()
                    timer?.cancel()
                    timer = MonTimer(dur, 1000)
                    timer?.start()
                }
            }

        }
    }

    //Création du timer, à chaque Tick les textes changent ainsi que la seekbar pour quelle bouge
    inner class MonTimer(duration: Long, intervale : Long): CountDownTimer(duration,intervale)
    {
        override fun onTick(millisUntilFinished: Long) {
            val currentPos = player?.currentPosition ?: 0
            val duration = player?.duration ?: 1
            tempDepart.text = DateUtils.formatElapsedTime(currentPos / 1000)
            tempFin.text = DateUtils.formatElapsedTime(duration / 1000)

            barprogress.max = duration.toInt()
            barprogress.progress = currentPos.toInt()

        }

        //Lorsque la musique fini, une autre reprend
        override fun onFinish() {
            player!!.play()
        }

    }

    //Changement des textes, dépendament de la musique en cours.
    fun changerText(player: ExoPlayer){
        txtArtiste.text = player.mediaMetadata.artist
        txtAlbum.text = player.mediaMetadata.albumTitle
        txtNom.text = player.mediaMetadata.title
        txtGenre.text = player.mediaMetadata.genre
    }

    //Lorsque l'on s'apprète à quitter l'application, une sauvegarde est effectué.
    override fun onPause() {
        super.onPause()
        // Sauvegarde complète de l'état
        val extras = HashMap<String, Any>()
        hashMap?.let { extras["musique"] = it }
        extras["currentIndex"] = player?.currentMediaItemIndex ?: 0
        extras["currentPosition"] = player?.currentPosition ?: 0L
        extras["volume"] = (player?.volume?.times(100))?.toInt() ?: 100
        extras["isPlaying"] = player?.isPlaying ?: false
        backgroundColor?.let { extras["background"] = it }
        val etat = EtatApplication(this::class.java.name, extras)
        SerialisationUtil.sauvegarderEtat(this, etat)

        player?.pause()
        timer?.cancel()
    }

    //Lorsque l'on quitte l'activité, le player est effacer et le timer arrêté.
    override fun onDestroy() {
        super.onDestroy()
        player!!.release()
        player = null
        timer?.cancel()
    }
}




