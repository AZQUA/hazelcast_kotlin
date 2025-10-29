package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.collection.IList
import com.hazelcast.collection.ISet


fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val maListe: IList<Int> = hazelcastInstance1.getList("ma-liste")
    val monSet: ISet<String> = hazelcastInstance1.getSet("mon-set")
    for (i in 1..10) {
        maListe.add(i)
    }
    monSet.add("Alpha")
    monSet.add("Bravo")
    monSet.add("Alpha")
    println("taille de ma liste : ${maListe.size}")
    println(maListe.joinToString(", "))
    println("Taille de mon set : ${monSet.size}")
    println(monSet.joinToString(", "))
    
    
    hazelcastInstance1.shutdown()
}