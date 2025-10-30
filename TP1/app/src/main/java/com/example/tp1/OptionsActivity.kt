package com.example.tp1

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OptionsActivity : AppCompatActivity() {
    lateinit var seekVolume : SeekBar
    lateinit var couleurback : TextView
    lateinit var boutonConfirme: Button
    lateinit var audioManager: AudioManager
    lateinit var txtVolume : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_options)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        couleurback = findViewById(R.id.txtBackgroundColor)
        boutonConfirme = findViewById(R.id.btnAppliquer)
        //setContentView(R.layout.activity_main)
        seekVolume = findViewById(R.id.seekBarVolume)
        txtVolume = findViewById(R.id.txtVolumeValue)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        // Example: Increase volume
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        seekVolume.max = maxVolume
        seekVolume.progress = currentVolume


        val ec = Ecouteur()
        seekVolume.setOnSeekBarChangeListener(ec)
        boutonConfirme.setOnClickListener {
            var i = Intent()
            var couleur = couleurback.text.toString().trim()
            var volume = seekVolume.progress
            if(estCouleurHexValide(couleur)){
                i.putExtra("couleur",couleur)
                i.putExtra("volume", volume)
                setResult(RESULT_OK,i)
                finish()
            }
            else{
                couleurback.error = "Le format de couleur est invalide. Format attendu : #RRGGBB ou #AARRGGBB"
            }


        }

    }
    fun estCouleurHexValide(couleur: String): Boolean {
        val regex = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
        return regex.matches(couleur)
    }

    inner class Ecouteur : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?,progress: Int,fromUser: Boolean) {
            if (fromUser) {
                txtVolume.text = progress.toString()
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }


    }
}