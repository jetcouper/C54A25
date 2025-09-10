package com.example.annexe1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var boutonAjouter:Button
    lateinit var boutonAfficher:Button
    lateinit var boutonQuitter:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        boutonAjouter = findViewById(R.id.btnAjouter)
        boutonAfficher = findViewById(R.id.btnAfficher)
        boutonQuitter = findViewById(R.id.btnQuitter)

        //1er
        val ec = Ecouteur()

        //2e etape
        boutonAjouter.setOnClickListener(ec)
        boutonAfficher.setOnClickListener(ec)
        boutonQuitter.setOnClickListener(ec)




    }

    //3 etape
    inner class Ecouteur : OnClickListener {
        override fun onClick(v: View?) {

            when(v){
                boutonQuitter -> finish()
                boutonAfficher -> {
                    val i = Intent(this@MainActivity,AfficherActivity::class.java)
                    startActivity(i)
                }
                boutonAjouter -> {
                    val j = Intent(this@MainActivity,AjouterActivity::class.java)
                    startActivity(j)
                }
            }


        }
    }


}