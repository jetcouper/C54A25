package com.example.tp1

import com.beust.klaxon.Json

//Liste de musique pour Klaxon
class ListeMusiques {
    @Json(name = "music") //si tu veux prendre un nom different plutot qu'accessoires, important
    var musiques: List<Musique> = emptyList()
}