package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.example.hazelcast_project.IncrementingProcessor
import com.hazelcast.core.IExecutorService
import java.io.Serializable
import java.util.concurrent.Callable
import com.hazelcast.cp.lock.FencedLock
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) = runBlocking {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val verrou : FencedLock = hazelcastInstance1.cpSubsystem.getLock("mon-verrou")
    val jobA = launch {
        println("Tentative de lock de l'utilisateur A")
        if (verrou.tryLock(2, TimeUnit.SECONDS)) {
            try {
                println("J'ai le verrou ! je fais mon travail critique...")
                delay(5000)
                println("J'ai fini mon travail.")
            } finally {
                verrou.unlock()
                println("Verrou rendu par l'utilisateur A")
            }
        }
        else {
            println("Timeout dépassé, abandon")
        }
    }
    val jobB = launch {
        println("Tentative de lock de l'utilisateur B")
        if (verrou.tryLock(2, TimeUnit.SECONDS)) {
            try {
                println("J'ai le verrou ! je fais mon travail critique...")
                delay(5000)
                println("J'ai fini mon travail.")
            } finally {
                verrou.unlock()
                println("Verrou rendu par l'utilisateur B")
            }
        }
        else {
            println("Timeout dépassé, abandon")
        }
    }
    jobA.join()
    jobB.join()
    hazelcastInstance1.shutdown()
}

class MaTacheSerializable : Callable<String>, Serializable {
    override fun call(): String {
        // Code exécuté sur le membre distant
        println("--- Je m'exécute sur un membre du cluster ! ---")
        Thread.sleep(1000) // Simule un long calcul
        return "Le calcul est terminé !"
    }
}