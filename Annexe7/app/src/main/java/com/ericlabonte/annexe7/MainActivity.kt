package com.ericlabonte.annexe7


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.iterator


class MainActivity : AppCompatActivity() {

    lateinit var parent: LinearLayout
    var mImageView: ImageView? = null
    lateinit var btnAppel : Button
    lateinit var btnMessage : Button
    lateinit var btnAchat : Button
    lateinit var btnHawke : Button
    lateinit var btnPhoto : Button
    var lanceur : ActivityResultLauncher<Intent>? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ec = Ecouteur()
        parent = findViewById(R.id.main)
        mImageView = findViewById(R.id.imageView)

        btnAchat = findViewById(R.id.boutonLivre)
        btnAppel = findViewById(R.id.boutonAppel)
        btnPhoto = findViewById(R.id.boutonPhoto)
        btnHawke = findViewById(R.id.boutonVille)
        btnMessage = findViewById(R.id.boutonMessage)

        for ( enfant in parent )
        {
            if ( enfant is Button)
                enfant.setOnClickListener(ec)
        }

        lanceur = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), CallBackPhoto())




    }
    inner class CallBackPhoto : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult) {
            if(result.resultCode == Activity.RESULT_OK){
                val extras = result.data?.extras
                val bitmap = extras?.get("data") as Bitmap?
                if(bitmap != null){
                    mImageView?.setImageBitmap(bitmap)
                }
            }
        }

    }


    inner class Ecouteur : OnClickListener {
        override fun onClick(v: View?) {
            if (v == btnAppel) // equals
            {
                val i = Intent( Intent.ACTION_DIAL, Uri.parse("tel:+7897895555" ))
                startActivity(i)
            }
            else if ( v == btnHawke)
            {
                val i = Intent ( Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Hawkesbury, +on, +ca"))
                startActivity(i)
            }
            else if (v == btnAchat){
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("leslibraires.ca"))
                startActivity(i)
            }
            else if (v == btnPhoto){
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                lanceur?.launch(i)
            }
            else if (v == btnMessage){
                val i = Intent(Intent.ACTION_SEND)
                i.putExtra("EXTRA_TEXT", "Allo")
                startActivity(i)

            }
        }
    }
}