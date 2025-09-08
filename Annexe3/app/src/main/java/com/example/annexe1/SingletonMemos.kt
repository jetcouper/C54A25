package com.example.annexe1

object SingletonMemos {
    var liste: ArrayList<Memo> = ArrayList()

    fun ajouterMemo(memo: Memo){

        liste.add(memo)
    }

}