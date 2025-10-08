package com.example.tp1

import com.beust.klaxon.Json

class ListeMusiques {
    @Json(name = "accessoires") //si tu veux prendre un nom different plutot qu'accessoires, important
    var articles: List<Musique> = emptyList()
}