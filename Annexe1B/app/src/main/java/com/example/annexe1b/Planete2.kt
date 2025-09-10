package com.example.annexe1b

data class Planete2(private var nom:String, private var satellite:Int) {

    //Si on veut, utilisé un bloc init
    private var nbHabitants:Long = 0
    init {
        if(nom == "Terre")
            nbHabitants = 8_000_000_000;
    }
    //constructeur secondaire: doit se baser sur le primaire
    constructor(nom:String, satellite: Int, nbHabitants:Long) :this(nom,satellite){
        this.nbHabitants = nbHabitants;
    }

    //Facultatif



}