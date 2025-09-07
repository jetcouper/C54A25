package com.example.annexe1b

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Scanner
import java.util.Vector

class MainActivity : AppCompatActivity() {

    lateinit var questionA: TextView
    lateinit var questionB: TextView
    lateinit var questionC: TextView
    lateinit var questionDTexte: EditText
    lateinit var questionDButton: Button
    lateinit var question3: TextView
    lateinit var questionPlanete: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        questionA = findViewById(R.id.txtReponseA)
        questionA.setText(questionA())


        questionB = findViewById(R.id.txtReponseB)
        questionB.setText(questionB())

        questionC = findViewById(R.id.txtReponseC)
        questionC.setText(questionC())

        questionDTexte = findViewById(R.id.txtNom)
        questionDButton = findViewById(R.id.btnQuestionD)

        question3 = findViewById(R.id.txtReponseScanner)
        question3.setText(question3A())

        questionPlanete = findViewById(R.id.txtPlanete)
        questionPlanete.setText(planeteCount().toString())

        questionDButton.setOnClickListener{
            questionD()
        }

    }
    fun questionA():String{

        var nombre : Int = 0
        val ofi = openFileInput("texte.txt")
        val isr = InputStreamReader(ofi)
        val br = BufferedReader(isr)

        br.use {
            for(line in br.lines()){
                nombre++
            }
        }
        //Autre version
//        br.use {
//            nombre = br.readLines().size
//        }


        return nombre.toString()
    }
    fun questionB():String{
        var nombreChar : Int = 0
        val ofi = openFileInput("texte.txt")
        val isr = InputStreamReader(ofi)
        val br = BufferedReader(isr)
        br.use {
            for(char in br.readText()){
                if(char != '\r' && char != '\n')
                    nombreChar++
            }
        }



        return nombreChar.toString()

    }
    fun questionC():String{
        var nombreChar : Int = 0
        val ofi = openFileInput("texte.txt")
        val isr = InputStreamReader(ofi)
        val br = BufferedReader(isr)
        br.use {
            //Autre façon
            //nombreChar = br.readText().count{c -> c == 'c'}

            for(char in br.readText()){
                if(char == 'c' || char == 'C'){
                    nombreChar++
                }
            }
        }

        return nombreChar.toString()

    }

    fun questionD(){
        var nom : String = questionDTexte.text.toString()
        val fos = openFileOutput("texte.txt", MODE_APPEND) //APPEND, Ajoute à la suite du contenue déjà là.
        val osw = OutputStreamWriter(fos)
        val bw = BufferedWriter(osw)
        bw.newLine()
        bw.write(nom)
        bw.close()
        questionDTexte.text.clear()
        Toast.makeText(this,  "$nom à été ajouté.", Toast.LENGTH_LONG ).show()
    }

    fun question3A():String{


        var nombreMot : Int = 0
        val ofi = openFileInput("texte.txt")
        val isr = InputStreamReader(ofi)
        val br = BufferedReader(isr)

        val scanner = Scanner(br)

        while (scanner.hasNext()){
            scanner.next()
            nombreMot++

        }
        scanner.close()
        br.close()

        return nombreMot.toString()
    }

    fun planeteCount():Int{
        val ofi = openFileInput("texte2.txt")
        val isr = InputStreamReader(ofi)
        val br = BufferedReader(isr)
        val listePlanete: Vector<Planete> = Vector()

        val scanner = Scanner(br)

        while (scanner.hasNextLine()){
            val ligne = scanner.nextLine()
            val parti = ligne.split("\\s+".toRegex())

            if(parti.size == 2){
                val nom:String = parti[0]
                val nombre:Int = parti[1].toInt()
                val planet = Planete(nom,nombre)
                listePlanete.add(planet)
            }
        }
        return listePlanete.count()
    }







}