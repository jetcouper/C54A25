package examen1.antoine.dextraze

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    lateinit var btnAnalise: Button
    lateinit var txtPomme : TextView
    lateinit var txtCereal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        btnAnalise = findViewById(R.id.button)
        txtCereal = findViewById(R.id.texteCereales)
        txtPomme = findViewById(R.id.textePommes)
        deserialise(this@MainActivity)



        btnAnalise.setOnClickListener {
            var produitPomme = retourneMeilleurPrix("Sac Pommes")
            var produitCereal = retourneMeilleurPrix("Céréales")

            txtPomme.text = produitPomme!!.nomMarcher + " " + produitPomme!!.prix.toString() + " $"
            txtCereal.text = produitCereal!!.nomMarcher + " " + produitCereal!!.prix.toString() + " $"
            btnAnalise.isEnabled = false
        }

    }

    fun lireFichier(): ArrayList<Produit>{
        var listeProduit = ArrayList<Produit>()
        val ofi: InputStream = getResources().openRawResource(R.raw.circulaires)
        val scanner = Scanner(ofi)
        scanner.useDelimiter(",")


        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val lineScanner = Scanner(line)
            lineScanner.useDelimiter(",")

            if (lineScanner.hasNext()) {
                val val1 = if (lineScanner.hasNext()) lineScanner.next() else ""
                val val2 = if (lineScanner.hasNext()) lineScanner.next() else ""
                val val3 = if (lineScanner.hasNext()) lineScanner.next() else ""


                // On ajoute dans la liste
                listeProduit.add(Produit(val1, val2.toFloat(), val3))
            }
            lineScanner.close()
        }
        scanner.close()
        return listeProduit
    }

    fun retourneMeilleurPrix(nom: String): Produit?{
        var produitChoisi : Produit? = null
        var listeProduit = ArrayList<Produit>()
        listeProduit = lireFichier()

        for(produit in listeProduit){
            if(produit.nom == nom){
                if(produitChoisi == null){
                    produitChoisi = produit
                }
                else
                {
                    if(produit.prix < produitChoisi.prix){
                        produitChoisi = produit
                    }
                }
            }

        }
        return produitChoisi
    }
    fun serialisation(contexte: Context){ //Sauvegarder dans le fichier
        var listeProduit = ArrayList<String>()
        try {
            val fos = contexte.openFileOutput("sauvegarde.ser", Context.MODE_PRIVATE) //Private pour écraser
            val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
            oos.use {//(Lambda)Pour ne pas faire .close()
                listeProduit.add(txtPomme.text.toString())
                listeProduit.add(txtCereal.text.toString())
                oos.writeObject(listeProduit)
            }
        }
        catch (io: IOException){
            io.printStackTrace()
        }
    }
    fun deserialise(contexte: Context){ //Récupérer fichier
        try {
            val fos = contexte.openFileInput("sauvegarde.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                val listeString = ois.readObject() as ArrayList<String>

                var produitChoisiPomme : String
                var produitChoisiCereal : String
                produitChoisiPomme = listeString[0]
                produitChoisiCereal = listeString[1]



                txtPomme.text = produitChoisiPomme
                txtCereal.text = produitChoisiCereal
                if(txtCereal != null && txtPomme != null){
                    btnAnalise.isEnabled = false
                }

            }
        }catch (f: FileNotFoundException){
            f.printStackTrace()
            Toast.makeText(this,"Il n'y a pas de ficher", Toast.LENGTH_LONG).show()
        }
    }


    override fun onStop() {
        super.onStop()
        serialisation(this@MainActivity)
    }
}