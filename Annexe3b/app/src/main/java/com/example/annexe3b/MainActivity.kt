package com.example.annexe3b

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {

    lateinit var sonnerieseek:SeekBar
    lateinit var mediaseek:SeekBar
    lateinit var notificationsseek:SeekBar

    var volume:Volume? = null //Type Volume Nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sonnerieseek = findViewById(R.id.seekBarSonnerie)
        mediaseek = findViewById(R.id.seekBarMedia)
        notificationsseek = findViewById(R.id.seekBarNotifications)

        //Récupérer s'il y a un fichier de sérialisation
        deserialise(this@MainActivity)


    }

    fun serialisation(contexte: Context){
        try {
            val fos = contexte.openFileOutput("serialisation.ser", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
            oos.use {
                val volume = Volume(sonnerieseek.progress,mediaseek.progress,notificationsseek.progress)
                oos.writeObject(volume)
            }
        }
        catch (io: IOException){
            io.printStackTrace()
        }



    }

    fun deserialise(contexte: Context){
        try {
            val fos = contexte.openFileInput("serialisation.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                volume = ois.readObject() as Volume
                mediaseek.progress = volume!!.media //On prend la responsabilité qu'il n'est pas null
                sonnerieseek.progress = volume!!.sonnerie
                notificationsseek.progress = volume!!.notifications
            }
        }catch (f:FileNotFoundException){
            f.printStackTrace()
            Toast.makeText(this@MainActivity,"Il n'y a pas de ficher",Toast.LENGTH_LONG).show()
            //50% par défaut
            sonnerieseek.progress = 50
            mediaseek.progress = 50
            notificationsseek.progress = 50
        }



    }


    override fun onStop() {

        serialisation(this@MainActivity)


        super.onStop()
    }
}