package com.example.annexe1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedWriter
import java.io.FileReader
import java.io.OutputStreamWriter


class AjouterActivity : AppCompatActivity() {

    lateinit var boutonAjouterMemo: Button;
    lateinit var chamMemo: EditText

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

        //Simplification interface fonctionnelle + lambda + lambda comme dernier paramètre --> à revoir
        boutonAjouterMemo.setOnClickListener{
            //println("Allo")
            var texteMemo = chamMemo.text.toString()
            //Vérifier si TexteMémo est vide ou s'il est différent de 0(lenght)
            if(!texteMemo.isEmpty()){
                //MODE_APPEND -> mode ajouter
                val fos = openFileOutput("fichier.txt", MODE_APPEND) //APPEND, Ajoute à la suite du contenue déjà là.
                val osw = OutputStreamWriter(fos)
                val bw = BufferedWriter(osw)

                bw.write(texteMemo)
                bw.newLine()

                bw.close()

                chamMemo.text.clear()

                finish()//Pour revenir au menu principale
            }

        }

    }



    override fun onStop() {
        super.onStop()
        finish()
    }
}