package com.example.annexe4

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
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
        deserialise(this@MainActivity)
        //Ma version
//        if(texte.text == ""){
//            if(savedInstanceState != null){
//                user = savedInstanceState!!.getSerializable("user") as Utilisateur
//                texte.text = "Bonjour " + user!!.prenom + " " + user!!.nom
//            }
//        }

        //Version Prof savedInstanceState
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            user = savedInstanceState?.getSerializable("user",Utilisateur::class.java)
//        }
//        else{
//            user = savedInstanceState?.getSerializable("user") as Utilisateur?
//        }
//          // ?: elvis --> ce qu'on veut si le membre de gauche est null
//        texte.text = "Bonjour ${user?.prenom?:" "} ${user?.nom?:" "}"



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
        //Ma version
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        if(user != null){
//            val utilisateur = Utilisateur(user!!.nom,user!!.prenom)
//
//            outState.putSerializable("user",utilisateur)
//        }
//    }
      //Version du prof
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        //Conserver L'utilisateur si le cycle de vie est refait
//        outState.putSerializable("user",user)
//
//    }
    fun deserialise(contexte: Context){ //Récupérer fichier
        try {
            val fos = contexte.openFileInput("serialisationUtil.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                val util = ois.readObject() as Utilisateur
                user = util
                texte.text = "Bonjour ${user?.prenom} ${user?.nom}"

            }
        }catch (f: FileNotFoundException){
            f.printStackTrace()
            Toast.makeText(this@MainActivity,"Il n'y a pas de ficher", Toast.LENGTH_LONG).show()
        }
    }
    fun serialisation(contexte: Context){ //Sauvegarder dans le fichier
        try {
            val fos = contexte.openFileOutput("serialisationUtil.ser", Context.MODE_PRIVATE) //Private pour écraser
            val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
            oos.use {//(Lambda)Pour ne pas faire .close()
                oos.writeObject(user)
            }
        }
        catch (io: IOException){
            io.printStackTrace()
        }
    }


    override fun onStop() {
        super.onStop()
        serialisation(this@MainActivity)

    }
}
