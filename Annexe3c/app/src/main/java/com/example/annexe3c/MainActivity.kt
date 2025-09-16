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
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.collections.mutableListOf


class MainActivity : AppCompatActivity() {

    lateinit var dent1: LinearLayout
    lateinit var dent2: LinearLayout

    var dent:Dent? = null
    var dents = mutableListOf<Dent>()



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


        //Récupérer s'il y a un fichier de sérialisation
        deserialise(this@MainActivity)




    }
    fun deserialise(contexte: Context){
        try {
            val fos = contexte.openFileInput("serialisationDent.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                val dents = ois.readObject() as List<Dent>
                val d1 = dents[0]
                for (i in 0 until dent1.childCount) {

                    if(i == 0){
                        (dent1.getChildAt(i) as EditText).setText(d1!!.noDent.toString())
                    }
                    if(i == 1){
                        (dent1.getChildAt(i) as CheckBox).isChecked = d1!!.canal
                    }
                    if(i == 2){
                        (dent1.getChildAt(i) as EditText).setText(d1!!.note.toString())
                    }

                }
                val d2 = dents[1]
                for (i in 0 until dent2.childCount) {

                    if(i == 0){
                        (dent2.getChildAt(i) as EditText).setText(d2!!.noDent.toString())
                    }
                    if(i == 1){
                        (dent2.getChildAt(i) as CheckBox).isChecked = d2!!.canal
                    }
                    if(i == 2){
                        (dent2.getChildAt(i) as EditText).setText(d2!!.note.toString())
                    }

                }
            }
        }catch (f: FileNotFoundException){
            f.printStackTrace()
            Toast.makeText(this@MainActivity,"Il n'y a pas de ficher", Toast.LENGTH_LONG).show()
        }
    }


    fun serialisation(contexte: Context){
        try {


            val fos = contexte.openFileOutput("serialisationDent.ser", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
            oos.use {
                val nodent = 0
                val canal = false
                val note = ""

                val d1 = Dent(nodent,canal,note)
                for (i in 0 until dent1.childCount) {

                    if(i == 0){
                        d1!!.noDent = (dent1.getChildAt(i) as EditText).text.toString().toInt()
                    }
                    if(i == 1){
                        d1!!.canal = (dent1.getChildAt(i) as CheckBox).isChecked
                    }
                    if(i == 2){
                        d1!!.note = (dent1.getChildAt(i) as EditText).text.toString()
                    }

                }
                dents.add(d1)
                val d2 = Dent(nodent,canal,note)
                for (i in 0 until dent2.childCount) {

                    if(i == 0){
                        d2!!.noDent = (dent2.getChildAt(i) as EditText).text.toString().toInt()
                    }
                    if(i == 1){
                        d2!!.canal = (dent2.getChildAt(i) as CheckBox).isChecked
                    }
                    if(i == 2){
                        d2!!.note = (dent2.getChildAt(i) as EditText).text.toString()
                    }

                }
                dents.add(d2)

                oos.writeObject(dents)


            }
            dent = null;
        }
        catch (io: IOException){
            io.printStackTrace()
        }
    }
    override fun onStop() {

        serialisation(this@MainActivity)


        super.onStop()
    }
}