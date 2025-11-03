package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.config.Config
import com.hazelcast.config.MapConfig
import com.hazelcast.config.MapStoreConfig
import com.example.hazelcast_project.PersonMapStore
import com.example.hazelcast_project.Person

fun main(args: Array<String>) {
    val mapStoreConfig = MapStoreConfig()
    mapStoreConfig.setImplementation(PersonMapStore()) 
    mapStoreConfig.setWriteDelaySeconds(0) 
    val config = Config()
    config.getMapConfig("personnes-db").setMapStoreConfig(mapStoreConfig)
    println("Démarrage de l'instance avec MapStore activé...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance(config)
    val personnesMap1: IMap<String, Person> = hazelcastInstance1.getMap("personnes-db")
    println("\n--- TEST PARTIE 1 : Écriture en BDD ---")
    val alice = Person("Alice", 30, "Paris")
    personnesMap1.put("alice-001", alice)
    println("Donnée 'alice-001' est dans la RAM : ${personnesMap1.get("alice-001")}")
    println("\nArrêt de l'instance 1 (RAM vidée)...")
    hazelcastInstance1.shutdown()
    println("\n--- TEST PARTIE 2 : Re-démarrage et lecture ---")
    val hazelcastInstance2 = Hazelcast.newHazelcastInstance(config)
    val personnesMap2: IMap<String, Person> = hazelcastInstance2.getMap("personnes-db")
    println("Tentative de lecture de 'alice-001' depuis la nouvelle instance...")
    val aliceFromDb = personnesMap2.get("alice-001") 
    println("Donnée récupérée depuis la BDD : $aliceFromDb")
    if (aliceFromDb != null) {
        println("SUCCÈS ! 🚀 La donnée a été persistée et rechargée.")
    } else {
        println("ÉCHEC. La donnée n'a pas été trouvée.")
    }
    hazelcastInstance2.shutdown()
}

