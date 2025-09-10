package com.example.annexe1

import android.content.Context
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream

//Object: Pour un singleton
object SingletonMemos {
    private var liste: ArrayList<Memo> = ArrayList()

    fun ajouterMemo(memo: Memo){
        liste.add(memo)
    }

    fun getListe(): ArrayList<Memo>{
        return liste
    }

    fun serialiserListe(contexte:Context){
        val fos = contexte.openFileOutput("serialisation.ser",Context.MODE_PRIVATE)
        val oos = ObjectOutputStream(fos)//Buffer(Tampon) spécial pour les objets
        oos.use {
            oos.writeObject(liste)
        }
    }
    fun recupererListe(contexte: Context):ArrayList<Memo>{

        if(liste.isEmpty()){
            val fos = contexte.openFileInput("serialisation.ser")
            val ois = ObjectInputStream(fos)//Buffer(Tampon) spécial pour les objets
            ois.use {
                liste = ois.readObject() as ArrayList<Memo>

            }
        }
        return ArrayList(liste) //Retourne une copie de sécurité de notre liste
    }


}