package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.example.hazelcast_project.IncrementingProcessor
import com.hazelcast.core.IExecutorService
import java.io.Serializable
import java.util.concurrent.Callable

fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val monExecutor: IExecutorService = hazelcastInstance1.getExecutorService("Mon-executor")
    val futureResultat = monExecutor.submit( MaTacheSerializable() )

    // 2. Attendre et réclamer le résultat
    println("En attente du résultat...")
    val resultat = futureResultat.get() // <-- Cette ligne va bloquer jusqu'à ce que la tâche soit finie

    println("Le membre distant a répondu : $resultat")
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