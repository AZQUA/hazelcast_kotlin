package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.collection.IList
import com.hazelcast.collection.ISet


fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val hazelcastInstance2 = Hazelcast.newHazelcastInstance()
    val maMap1: IMap<String, String> = hazelcastInstance1.getMap("ma-Map")
    val maMap2: IMap<String, String> = hazelcastInstance2.getMap("ma-Map")
    maMap1.put("test-cle","donne-instance-1")
    val valeurLue = maMap2.get("test-cle")
    println(valeurLue)
    hazelcastInstance1.shutdown()
    hazelcastInstance2.shutdown()
}