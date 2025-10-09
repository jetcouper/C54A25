package com.example.tp1

import java.io.Serializable
import kotlin.time.Duration

data class Musique(val id:String,
                   val site:String,
                   val album:String,
                   val genre:String,
                   val image:String,
                   val title:String,
                   val artist:String,
                   val source:String,
                   val duration: Int,
                   val trackNumber:Int,
                   val totalTrackCount:Int

    ) {
}