package com.example.tp1

import EtatApplication
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OptionsActivity : AppCompatActivity() {
    lateinit var seekVolume : SeekBar
    lateinit var couleurback : TextView
    lateinit var boutonConfirme: Button
    lateinit var audioManager: AudioManager
    lateinit var txtVolume : TextView
    lateinit var main : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_options)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        main = findViewById(R.id.main)
        couleurback = findViewById(R.id.txtBackgroundColor)
        boutonConfirme = findViewById(R.id.btnAppliquer)
        seekVolume = findViewById(R.id.seekBarVolume)
        txtVolume = findViewById(R.id.txtVolumeValue)

        //Restauration du dernier événement avec ces composantes
        val etat = SerialisationUtil.restaurerEtat(this)
        etat?.extraIntent?.let { extras ->
            val savedVolume = extras["volume"] as? Int ?: 0
            val savedCouleur = extras["background"] as? String ?: "#FFFFFF"

            txtVolume.text = savedVolume.toString()
            couleurback.text = savedCouleur
            seekVolume.progress = savedVolume
            main.setBackgroundColor(savedCouleur.toColorInt())
        }

        //Si je reçois quelques chose du MainActivity, alors peupler ses éléments.
        if(!intent!!.getStringExtra("couleur").isNullOrEmpty() || intent!!.getIntExtra("volume", -1) != -1){
            txtVolume.text = intent!!.getIntExtra("volume", -1).toString()
            couleurback.text = intent!!.getStringExtra("couleur").toString()
            main.setBackgroundColor(intent!!.getStringExtra("couleur")!!.toColorInt())
        }
        //Le système de volume générale du téléphone
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        // Le volume actuel
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        // Le volume maximale
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        seekVolume.max = maxVolume
        seekVolume.progress = currentVolume


        val ec = Ecouteur()
        seekVolume.setOnSeekBarChangeListener(ec)
        boutonConfirme.setOnClickListener {
            val couleur = couleurback.text.toString().trim()
            val volume = seekVolume.progress

            if(estCouleurHexValide(couleur)){
                val intent = Intent()
                intent.putExtra("couleur", couleur)
                intent.putExtra("volume", volume)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                couleurback.error = "Le format de couleur est invalide. Format attendu : #RRGGBB ou #AARRGGBB"
            }


        }

    }
    //Validation pour voir si le reghex est valide.
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
    override fun onStop() {
        super.onStop()
        //À la fin de l'activité, une sérialisation s'éffectura pour garder en mémoire la dernière activité ouvert.
        val extras = HashMap<String, Any>()
        extras["volume"] = seekVolume.progress
        extras["couleur"] = couleurback.text.toString()
        extras["seekVolume"] = seekVolume.progress

        val etat = EtatApplication(
            activiteCourante = this::class.java.name,
            extraIntent = extras
        )

        SerialisationUtil.sauvegarderEtat(this, etat)
    }
}