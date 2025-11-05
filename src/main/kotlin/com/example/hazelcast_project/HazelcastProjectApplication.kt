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
    var error:Int = 0
    val jobP = launch {
        println("[Producteur 🧑‍🌾] Démarrage...")
        for (i in 1..20) {
            tacheAFaire.add(i)
            
            println("[Producteur 🧑‍🌾] Tâche $i ajoutée.")
        }
        println("[Producteur 🧑‍🌾] Signaux envoyés. Terminé.")
        println(tacheAFaire.joinToString())
    }
    
    val jobA = launch {
        println("[Worker A 👷] Prêt au travail.")
        while (tacheAFaire.size!=0) {
            println(tacheAFaire.joinToString())
            val tacheActuelle:Int = tacheAFaire.take()
            delay(10)
            try {
                // simulation d'une erreur
                if (tacheActuelle == 13 && error==0) {
                    println("[Worker A 👷] ERREUR VOLONTAIRE sur la tâche 13 !")
                    error = 1
                    throw RuntimeException("C'est 13 ! J'ai peur !")
                    
                }
                println("[Worker A 👷] Traitement de la tâche $tacheActuelle...")
                resultat.put(tacheActuelle, "Résultat du Worker A pour $tacheActuelle")
            } catch(e: Exception) {
                println("[Worker A 👷] ERREUR attrapée : ${e.message}. Tâche $tacheActuelle envoyée aux échecs.")
                tacheEchec.put(tacheActuelle)
                println(error)
            }
        }
        println("[Worker A 👷] Signal d'arrêt reçu. Arrêt.")
    }
    val jobB = launch {
        println("[Worker B 👷] Prêt au travail.")
        while (tacheAFaire.size!=0) {
            println(tacheAFaire.joinToString())
            val tacheActuelle:Int = tacheAFaire.take()
            delay(10)
            try {
                // simulation d'une erreur
                if (tacheActuelle == 13 && error==0) {
                    println("[Worker B 👷] ERREUR VOLONTAIRE sur la tâche 13 !")
                    error = 1
                    throw RuntimeException("C'est 13 ! J'ai peur !")
                    
                }
                println("[Worker B 👷] Traitement de la tâche $tacheActuelle...")
                resultat.put(tacheActuelle, "Résultat du Worker B pour $tacheActuelle")
            } catch(e: Exception) {
                println("[Worker B 👷] ERREUR attrapée : ${e.message}. Tâche $tacheActuelle envoyée aux échecs.")
                tacheEchec.put(tacheActuelle)
                println(error)
            }
        }
        println("[Worker B 👷] Signal d'arrêt reçu. Arrêt.")
    }
    val jobC = launch {
        println("[Worker C 👷] Prêt au travail.")
        while (tacheAFaire.size!=0) {
            println(tacheAFaire.joinToString())
            val tacheActuelle:Int = tacheAFaire.take()
            delay(10)
            try {
                // simulation d'une erreur
                if (tacheActuelle == 13 && error==0) {
                    println("[Worker C 👷] ERREUR VOLONTAIRE sur la tâche 13 !")
                    error = 1
                    throw RuntimeException("C'est 13 ! J'ai peur !")
                    
                }
                println("[Worker C 👷] Traitement de la tâche $tacheActuelle...")
                resultat.put(tacheActuelle, "Résultat du Worker C pour $tacheActuelle")
            } catch(e: Exception) {
                println("[Worker C 👷] ERREUR attrapée : ${e.message}. Tâche $tacheActuelle envoyée aux échecs.")
                tacheEchec.put(tacheActuelle)
                println(error)
            }
        }
        println("[Worker C 👷] Signal d'arrêt reçu. Arrêt.")
    }
    val jobR = launch(Dispatchers.IO) {
        println("[Retry Worker 🧹] Surveillance des échecs...")
        delay(100)
        while (tacheAFaire.size!=0) {
            val tacheEchouee = tacheEchec.take()
            if (tacheEchouee == -1) {
                break
            }
            println("[Retry Worker 🧹] Tâche $tacheEchouee trouvée. Attente de 2s...")
            println("[Retry Worker 🧹] Remise de la tâche $tacheEchouee dans la file principale.")
            tacheAFaire.put(tacheEchouee) 
        }
        println("[Retry Worker 🧹] Signal d'arrêt reçu. Arrêt.")
    }
    println("\nMain : Attente de la fin de toutes les tâches (barrière)...")
    jobP.join()
    jobA.join()
    jobB.join()
    jobC.join()
    tacheEchec.put(-1)
    jobR.join()
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

