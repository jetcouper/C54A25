package com.example.annexe3c

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText

import android.widget.LinearLayout
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

    lateinit var dent1: LinearLayout
    lateinit var dent2: LinearLayout

    var dent:Dent? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dent1 = findViewById(R.id.dent1)
        dent2 = findViewById(R.id.dent2)







    }
    fun deserialise(contexte: Context){
        try {
            val fos = contexte.openFileInput("serialisationDent.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                volume = ois.readObject() as Volume
                mediaseek.progress = volume!!.media //On prend la responsabilité qu'il n'est pas null
                sonnerieseek.progress = volume!!.sonnerie
                notificationsseek.progress = volume!!.notifications
            }
        }catch (f: FileNotFoundException){
            f.printStackTrace()
            Toast.makeText(this@MainActivity,"Il n'y a pas de ficher", Toast.LENGTH_LONG).show()
            //50% par défaut
            sonnerieseek.progress = 50
            mediaseek.progress = 50
            notificationsseek.progress = 50
        }
    }


    fun serialisation(contexte: Context){
        try {

            for (i in 0 until dent1.childCount) {

                if(i == 0){
                    dent!!.noDent = (dent1.getChildAt(i) as EditText).text.toString().toInt()
                }
                if(i == 1){
                    dent!!.canal = (dent1.getChildAt(i) as CheckBox).isChecked
                }
                if(i == 2){
                    dent!!.note = (dent1.getChildAt(i) as EditText).text.toString()
                }

            }


            val fos = contexte.openFileOutput("serialisationDent.ser", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
            oos.use {
                val volume = Dent(sonnerieseek.progress,mediaseek.progress,notificationsseek.progress)
                oos.writeObject(volume)
            }
        }
        catch (io: IOException){
            io.printStackTrace()
        }
    }
}