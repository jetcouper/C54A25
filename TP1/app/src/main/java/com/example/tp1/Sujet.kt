package com.example.tp1

interface Sujet {
   fun ajouterObservateur(o:ObservateurChangement)
   fun enleverObservateur(o:ObservateurChangement)
   fun avertirObservateurs()
}
