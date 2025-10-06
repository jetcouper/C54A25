package com.example.appvolley

import com.beust.klaxon.Json

class ListeProduits() {
    @Json(name = "accessoires") //si tu veux prendre un nom different plutot qu'accessoires, important
    var articles: List<Produit> = emptyList()
}