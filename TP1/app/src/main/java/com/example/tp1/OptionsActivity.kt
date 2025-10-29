package com.example.tp1

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_options)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(R.layout.activity_main)

        // Get an instance of AudioManager
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // Example: Increase volume
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        if (currentVolume < maxVolume) {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_SHOW_UI // Optional: Show the system volume UI
            )
        }

        // Example: Decrease volume
        if (currentVolume > 0) {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_SHOW_UI
            )
        }

        // Example: Set a specific volume level
        val desiredVolumeLevel = 5 // Replace with your desired level
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            desiredVolumeLevel,
            AudioManager.FLAG_SHOW_UI
        )





    }
}