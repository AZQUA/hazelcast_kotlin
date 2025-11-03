package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.config.Config
import com.hazelcast.config.NearCacheConfig
import kotlin.system.measureTimeMillis
import com.hazelcast.nearcache.NearCacheStats
import com.hazelcast.map.LocalMapStats

fun main(args: Array<String>) {
    val nearCacheConfig = NearCacheConfig()
    nearCacheConfig.name = "default"
    nearCacheConfig.timeToLiveSeconds=60
    val config = Config()
    config.getMapConfig("ma-map-rapide").nearCacheConfig = nearCacheConfig
    println("Démarrage de l'instance avec le Near Cache activé...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance(config)
    val hazelcastInstance2 = Hazelcast.newHazelcastInstance(config)
    val map1: IMap<String, String> = hazelcastInstance1.getMap("ma-map-rapide")
    val map2: IMap<String, String> = hazelcastInstance2.getMap("ma-map-rapide")
    println("L'intance est prête...")
    map1.put("cle-1", "ma-donnee-rapide")
    println("\nDonnée mise en cache (put).")
    println("--- Première lecture (devrait être un 'MISS') ---")
    val temps1 = measureTimeMillis {
        map2.get("cle-1")
    }
    println("Temps de la 1ère lecture (réseau) : $temps1 ms")
    println("\n--- Deuxième lecture (devrait être un 'HIT') ---")
    val temps2 = measureTimeMillis {
        map2.get("cle-1")
    }
    println("Temps de la 2ème lecture (local) : $temps2 ms")
    val stats: NearCacheStats? = map2.localMapStats.nearCacheStats

    println("\n--- Statistiques Officielles du Near Cache ---")
    println("Nombre de 'HITS' (trouvé localement) : ${stats?.hits}")
    println("Nombre de 'MISSES' (allé chercher sur le réseau) : ${stats?.misses}")
    hazelcastInstance1.shutdown()
    hazelcastInstance2.shutdown()
}

