package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.collection.IList
import com.hazelcast.collection.ISet
import com.hazelcast.collection.IQueue
import kotlinx.coroutines.*
import com.hazelcast.topic.ITopic
import com.hazelcast.topic.Message
import com.hazelcast.topic.MessageListener


fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val Topic: ITopic<String> = hazelcastInstance1.getTopic("mon-topic")
    
    Topic.addMessageListener(object : MessageListener<String> {
        override fun onMessage(message: Message<String>) {
            println("[Abonné A] a reçu : ${message.messageObject}")
        }
    })
    Topic.addMessageListener(object : MessageListener<String> {
        override fun onMessage(message: Message<String>) {
            println("[Abonné B] a reçu : ${message.messageObject}")
        }
    })
    Topic.addMessageListener(object : MessageListener<String> {
        override fun onMessage(message: Message<String>) {
            println("[Abonné C] a reçu : ${message.messageObject}")
        }
    })
    println("\n--- Le Publisher envoie un message ! ---")
    Topic.publish("Bonjour tout le monde !")

}