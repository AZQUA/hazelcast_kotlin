package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import java.io.Serializable
import com.hazelcast.query.Predicates
import com.hazelcast.query.Predicate

fun main(args: Array<String>) {
    println("Démarrage...")
    
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val peopleMap: IMap<String , Person> = hazelcastInstance1.getMap("ma-people-map")
    
    peopleMap.put("p1", Person("Alpha", 25, "Paris"))
    peopleMap.put("p2", Person("Bravo", 35, "Lyon"))
    peopleMap.put("p3", Person("Charlie", 40, "Paris"))
    
    println("Création du filtre SQL : age >30")
    val filtreAge : Predicate<String, Person> = Predicates.sql("age > 30")
    val resultats = peopleMap.values(filtreAge)
    
    println("--- Personnes de plus de 30 ans ---")
    resultats.forEach { person ->
        println(person)
    }
    
    hazelcastInstance1.shutdown()
}

data class Person(
    val name: String,
    val age: Int,
    val city: String
) : Serializable
