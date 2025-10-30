package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.collection.IList
import com.hazelcast.collection.ISet
import com.hazelcast.collection.IQueue
import kotlinx.coroutines.*


fun main(args: Array<String>) = runBlocking {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val maQueue:IQueue<String> = hazelcastInstance1.getQueue("ma-queue")
    launch {
        for (i in 1..20) {
            maQueue.put("Message ${i}")
            println("Producteur : à envoyé un 'Message ${i}'.")
            delay(100)
        }
    }
    launch {
        for (i in 1..20) {
            val message = maQueue.take()
            println("Consomateur : à reçu un '$message'")
            delay(100)
        }
    }
    delay(20000)
    hazelcastInstance1.shutdown()
}