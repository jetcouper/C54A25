package com.example.annexe3c

fun main(args: Array<String>)
{
//    var mot:String? = null
//    println ( mot!!.length)

//    var mot:String = "abc"
//    var nombre:Int? = mot as Int
//    println(nombre)
    val listeAvecDesNull: List<String?> = listOf("hugo", "loic", null, "eddy")
    for (item in listeAvecDesNull) {
        item?.let { println(item.length) }
    }



}
