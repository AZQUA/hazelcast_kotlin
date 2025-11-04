package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.collection.IQueue
import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val tacheAFaire: IQueue<Int> = hazelcastInstance1.getQueue("tâche-à-faire")
    val resultat:IMap<Int, String> = hazelcastInstance1.getMap("resultats")
    val tacheEchec: IQueue<Int> = hazelcastInstance1.getQueue("tache-en-echec")
    val flag:Boolean = true
    val jobP = launch {
        println("[Producteur 🧑‍🌾] Démarrage...")
        for (i in 1..20) {
            tacheAFaire.add(i)
            
            println("[Producteur 🧑‍🌾] Tâche $i ajoutée.")
        }
        println("[Producteur 🧑‍🌾] Tâches envoyées. Envoi des 3 signaux d'arrêt...")
        repeat(3) {tacheAFaire.put(-1)}
        println("[Producteur 🧑‍🌾] Signaux envoyés. Terminé.")
        println(tacheAFaire.joinToString())
    }
    
    val jobA = launch {
        println("[Worker A 👷] Prêt au travail.")
        while (flag) {
            println(tacheAFaire.joinToString())
            val tacheActuelle:Int = tacheAFaire.take()
            delay(10)
            if (tacheActuelle == -1) {
                break
            }
            try {
                // simulation d'une erreur
                if (tacheActuelle == 13) {
                    println("[Worker A 👷] ERREUR VOLONTAIRE sur la tâche 13 !")
                    throw RuntimeException("C'est 13 ! J'ai peur !")
                }
                println("[Worker A 👷] Traitement de la tâche $tacheActuelle...")
                resultat.put(tacheActuelle, "Résultat du Worker A pour $tacheActuelle")
            } catch(e: Exception) {
                println("[Worker A 👷] ERREUR attrapée : ${e.message}. Tâche $tacheActuelle envoyée aux échecs.")
                tacheEchec.put(tacheActuelle)
            }
        }
        println("[Worker A 👷] Signal d'arrêt reçu. Arrêt.")
    }
    val jobB = launch {
        println("[Worker B 👷] Prêt au travail.")
        while (flag) {
            println(tacheAFaire.joinToString())
            val tacheActuelle:Int = tacheAFaire.take()
            delay(10)
            if (tacheActuelle == -1) {
                break
            }
            try {
                // simulation d'une erreur
                if (tacheActuelle == 13) {
                    println("[Worker B 👷] ERREUR VOLONTAIRE sur la tâche 13 !")
                    throw RuntimeException("C'est 13 ! J'ai peur !")
                }
                println("[Worker B 👷] Traitement de la tâche $tacheActuelle...")
                resultat.put(tacheActuelle, "Résultat du Worker B pour $tacheActuelle")
            } catch(e: Exception) {
                println("[Worker B 👷] ERREUR attrapée : ${e.message}. Tâche $tacheActuelle envoyée aux échecs.")
                tacheEchec.put(tacheActuelle)
            }
        }
        println("[Worker B 👷] Signal d'arrêt reçu. Arrêt.")
    }
    val jobC = launch {
        println("[Worker C 👷] Prêt au travail.")
        while (flag) {
            println(tacheAFaire.joinToString())
            val tacheActuelle:Int = tacheAFaire.take()
            delay(10)
            if (tacheActuelle == -1) {
                break
            }
            try {
                // simulation d'une erreur
                if (tacheActuelle == 13) {
                    println("[Worker C 👷] ERREUR VOLONTAIRE sur la tâche 13 !")
                    throw RuntimeException("C'est 13 ! J'ai peur !")
                }
                println("[Worker C 👷] Traitement de la tâche $tacheActuelle...")
                resultat.put(tacheActuelle, "Résultat du Worker C pour $tacheActuelle")
            } catch(e: Exception) {
                println("[Worker C 👷] ERREUR attrapée : ${e.message}. Tâche $tacheActuelle envoyée aux échecs.")
                tacheEchec.put(tacheActuelle)
            }
        }
        println("[Worker C 👷] Signal d'arrêt reçu. Arrêt.")
    }
    println("\nMain : Attente de la fin de toutes les tâches (barrière)...")
    jobP.join()
    jobA.join()
    jobB.join()
    jobC.join()
    println("Main : Toutes les tâches sont terminées !")
    println("\n--- Contenu final de la map 'resultats' ---")
    resultat.forEach { cle, valeur ->
        println("Clé: $cle -> Valeur: $valeur")
    }
    println("Taille totale des résultats : ${resultat.size}")
    println("\n--- Contenu final de la file 'tache-en-echec' ---")
    println("Nombres de tâches échouées : ${tacheEchec.size}")
    tacheEchec.forEach { tacheEchouee ->
        println("Tâches échouée : $tacheEchouee")
    }
    hazelcastInstance1.shutdown()
    println("Instance arrêtée. Programme terminé.")
}

