package com.example.annexe4

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var bouton: Button
    lateinit var texte: TextView
    var user: Utilisateur? = null
    lateinit var lanceur: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bouton = findViewById(R.id.btnConnaitre)
        texte = findViewById(R.id.txtBonjour)

        val ec = Ecouteur()
        bouton.setOnClickListener(ec)

        lanceur = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackText()
        )

    }
    //Le retour du boomerang, on revient ici après l'inscription du nom
    inner class CallBackText : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult) {
            var i = result.data
            user = i.getSerializableExtra("user",Utilisateur::class.java)


        }
    }
    inner class Ecouteur : View.OnClickListener {
        override fun onClick(v: View?) {
            lanceur.launch(Intent(this@MainActivity,RepondreActivity::class.java))


        }

    }

}