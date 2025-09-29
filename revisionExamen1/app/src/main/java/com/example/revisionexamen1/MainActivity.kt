package com.example.revisionexamen1

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Scanner

class MainActivity : AppCompatActivity() {
    lateinit var listeLangage: ArrayList<Langage>
    lateinit var texte1: TextView
    lateinit var texteDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        texte1 = findViewById(R.id.textNb)
        texteDate = findViewById(R.id.txtDate)

        var nombre2012 = 0
        nombre2012 = nblangage99()
        texte1.text = "Nombre de langage non comptabilisé en 2012: $nombre2012"

        deserialiseDate(this@MainActivity)


    }

    fun nblangage99(): Int {
        val listeLangage = ArrayList<Langage>()
        var nombre = 0
        val ofi: InputStream = getResources().openRawResource(R.raw.langage)
        val scanner = Scanner(ofi)
        scanner.useDelimiter(",")

        if (scanner.hasNextLine()) {
            scanner.nextLine()
        }
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val lineScanner = Scanner(line)
            lineScanner.useDelimiter(",")

            if (lineScanner.hasNext()) {
                val name = lineScanner.next()
                val val1 = if (lineScanner.hasNextInt()) lineScanner.nextInt() else 0
                val val2 = if (lineScanner.hasNextInt()) lineScanner.nextInt() else 0
                val val3 = if (lineScanner.hasNextInt()) lineScanner.nextInt() else 0

                // On ajoute dans la liste
                listeLangage.add(Langage(name, val1, val2, val3))
            }

            lineScanner.close()
        }
        scanner.close()

        for (langage in listeLangage) {
            if (langage.annee2012 == 99) {
                nombre++
            }

        }
        return nombre
    }

    fun deserialiseDate(contexte: Context) {
        try {
            val fos = contexte.openFileInput("serialisationTemp.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                val temps = it.readObject() as LocalDateTime
                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale.CANADA_FRENCH)
                texteDate.text = "dernière utilisation: ${temps.format(formatter)}"
            }
        } catch (f: FileNotFoundException) {
            f.printStackTrace()
            texteDate.text = "1er utilisation";
        }
    }
    fun serialisationDate(contexte: Context){
        try {
            val fos = contexte.openFileOutput("serialisationTemp.ser", MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
            oos.use {
                val date = LocalDateTime.now()
                oos.writeObject(date)
            }

        }
        catch (io: IOException){
            io.printStackTrace()
        }
    }


    override fun onStop() {
        super.onStop()
        serialisationDate(this@MainActivity)


    }
}