package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.example.hazelcast_project.IncrementingProcessor

fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val countersMap: IMap<String, Int> = hazelcastInstance1.getMap("compteur")
    
    countersMap.put("monCompteur",0)
    println("Compteur initialisé à : ${countersMap.get("monCompteur")}")

    println("Envoi de l'EntryProcesor pour incrémenter...")
    val resultat = countersMap.executeOnKey("monCompteur", IncrementingProcessor())
    println("Le processeur à retourné la nouvelle valeur : $resultat")
    println("Verification dans la map : ${countersMap.get("monCompteur")}")

    hazelcastInstance1.shutdown()
}

