package com.antoine.examen
import com.beust.klaxon.Json


class KlaxonChanson {
    @Json(name = "items") //si tu veux prendre un nom different plutot qu'accessoires, important
    var musiques: List<Chanson> = emptyList()
}