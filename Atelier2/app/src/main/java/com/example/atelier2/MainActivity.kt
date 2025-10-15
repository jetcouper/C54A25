package com.example.atelier2

import android.media.browse.MediaBrowser
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity : AppCompatActivity() {
    lateinit var lecteur: PlayerView
    lateinit var lecteurYoutube: YouTubePlayerView
    val mp3url = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/01_-_Intro_-_The_Way_Of_Waking_Up_feat_Alan_Watts.mp3"
    val video = "https://youtu.be/16y1AkoZkmQ"
    var player : ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lecteur = findViewById(R.id.playerView)
        player = ExoPlayer.Builder(this@MainActivity).build()

        //lecteur youtube (Fait par moi)
        lecteurYoutube = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(lecteurYoutube)
        val ec = Ecouteur()
        lecteurYoutube.addYouTubePlayerListener(ec)


    }

    override fun onStart() {
        super.onStart()
        //Lecteur audio
        lecteur.player = player
        val media = MediaItem.fromUri(mp3url)
        //player!!.addMediaItem(media) //Pour plusieurs
        player?.setMediaItem(media) //Pour un item
        player?.prepare()
        //player?.play() //Si tu veux démarrer tout de suite.
        player?.pause()

    }

    //Fait par moi.
    inner class Ecouteur : AbstractYouTubePlayerListener(){
        override fun onReady(youTubePlayer: YouTubePlayer) {
            val videoId = "16y1AkoZkmQ"
            youTubePlayer.cueVideo(videoId, 0f)
            //youTubePlayer.loadVideo(videoId,0f)
        }
    }



}