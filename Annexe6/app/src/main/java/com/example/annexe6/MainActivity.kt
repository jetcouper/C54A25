package com.example.annexe6

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var bouton: Button
    lateinit var image: ImageView

    var lanceur:ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bouton = findViewById(R.id.btnBoom)
        image = findViewById(R.id.img)



        bouton.setOnClickListener {
            val i = Intent( Intent.ACTION_OPEN_DOCUMENT)
            i.setType("image/*")
            lanceur!!.launch(i)

        }

        lanceur = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackImage()
        )

    }



    inner class CallBackImage : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult) {
            if(result.resultCode == Activity.RESULT_OK){
                val intentRetour: Intent = result.data!!
                val uri = intentRetour.data!!

                image.setImageURI(uri)



            }


        }

    }


}