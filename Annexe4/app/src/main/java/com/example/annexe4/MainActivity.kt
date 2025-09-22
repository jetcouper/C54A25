package com.example.annexe4

import android.content.Intent
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.Serializable

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

        //Le v n'est pas néssessaire ni l'écouteur
        bouton.setOnClickListener{v:View -> lanceur.launch(Intent(this@MainActivity,RepondreActivity::class.java))}


        lanceur = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackText() //Nom inventer
        )
//        if(texte.text == ""){
//            if(savedInstanceState != null){
//                user = savedInstanceState!!.getSerializable("user") as Utilisateur
//                texte.text = "Bonjour " + user!!.prenom + " " + user!!.nom
//            }
//        }

    }
    //Le retour du boomerang, on revient ici après l'inscription du nom
    inner class CallBackText : ActivityResultCallback<ActivityResult> {

        override fun onActivityResult(result: ActivityResult) {
            if(result.resultCode == RESULT_OK){
                var i = result.data
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    user = i!!.getSerializableExtra("user",Utilisateur::class.java)
                }
                else{
                    user = i!!.getSerializableExtra("user") as Utilisateur?
                }

                texte.text = "Bonjour ${user?.prenom} ${user?.nom}"
            }


        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        if(user != null){
//            val utilisateur = Utilisateur(user!!.nom,user!!.prenom)
//
//            outState.putSerializable("user",utilisateur)
//        }
//    }
}
