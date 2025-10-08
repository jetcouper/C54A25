package com.example.atelier3observerpattern

interface Sujet {
   fun ajouterObservateur(o:ObservateurChangement)
   fun enleverObservateur(o:ObservateurChangement)
   fun avertirObservateurs()
}
