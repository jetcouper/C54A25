package com.example.annexe1

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedWriter
import java.io.FileReader
import java.io.OutputStreamWriter
import java.time.LocalDate


class AjouterActivity : AppCompatActivity() {

    lateinit var boutonAjouterMemo: Button
    lateinit var chamMemo: EditText
    lateinit var boutonDate:Button
    lateinit var chamDate:TextView

    //La date d'écheance sera par défaut le lendemain
    var dateChoisi = LocalDate.now().plusDays(1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajouter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        boutonAjouterMemo = findViewById(R.id.btnAjouterMemo)
        chamMemo = findViewById(R.id.txtAjout)
        boutonDate = findViewById(R.id.btnDate)
        chamDate = findViewById(R.id.txtDate)
        chamDate.setText(dateChoisi.toString())
        val ec = Ecouteur()
        boutonAjouterMemo.setOnClickListener(ec)
        boutonDate.setOnClickListener(ec)

    }

inner class Ecouteur:OnClickListener, OnDateSetListener{
    override fun onClick(v: View?) {
        if(v == boutonDate){
            val d = DatePickerDialog(this@AjouterActivity)
            d.setOnDateSetListener(this)
            // On veut l'afficher
            d.show()
        }
        else if(v == boutonAjouterMemo){
            //Créer un objet mémo
            val memo = Memo(chamMemo.text.toString(),dateChoisi)

            //Autre façon
            //SingletonMemos.ajouterMemo(Memo(chamMemo.text.toString(),dateChoisi))

            //L'ajouter à notre liste(Singleton)
            SingletonMemos.ajouterMemo(memo)
            //Mettre a jour le fichier de sérialisation
            SingletonMemos.serialiserListe(this@AjouterActivity)
            chamMemo.text.clear()
            chamDate.text = ""
            finish()
        }



    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //Aller chercher le message et la date(LocalDate)
        dateChoisi = LocalDate.of(year,month+1,dayOfMonth)

        //Afficher la date dans le TextView
        chamDate.setText(dateChoisi.toString())

    }

}

    override fun onStop() {
        super.onStop()
        finish()
    }
}