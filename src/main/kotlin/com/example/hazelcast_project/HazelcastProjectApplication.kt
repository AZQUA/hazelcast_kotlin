package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap


fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val maMap: IMap<String, Int> = hazelcastInstance1.getMap("ma-map")
    maMap.put("1",1)
    maMap.put("5",5)
    maMap.put("4",4)
    maMap.put("3",3)
    maMap.put("2",2)
    println(maMap.get("1"))
    println(maMap.get("2"))
    println(maMap.get("3"))
    println(maMap.get("4"))
    println(maMap.get("5"))
    maMap.remove("1")
    maMap.forEach { cle, valeur ->
        println("cle : $cle , valeur $valeur")
    }
    hazelcastInstance1.shutdown()
}

