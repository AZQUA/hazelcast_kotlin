package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.config.Config
import com.hazelcast.config.SplitBrainProtectionConfig
import com.hazelcast.config.MapConfig

fun main(args: Array<String>) {
    println("Démarrage...")

    val quorum = SplitBrainProtectionConfig()
    quorum.name = "mon-quorum-par-defaut"
    quorum.isEnabled = true
    quorum.minimumClusterSize = 2

    val mapConfig = MapConfig("ma-map-protegee")
    mapConfig.splitBrainProtectionName = "mon-quorum-par-defaut"
    
    val config = Config()
    config.addSplitBrainProtectionConfig(quorum)
    config.addMapConfig(mapConfig)

    println("Démarage d'un noeud ...")
    val hazelcastInstance = Hazelcast.newHazelcastInstance(config)
    val mapProtegee:IMap<String,String> = hazelcastInstance.getMap("ma-map-protegee")
    
    println("Nœud démarré. Le cluster a ${hazelcastInstance.cluster.members.size} membres.")
    try {
        println("Tentative d'écriture...")
        mapProtegee.put("cle-1", "valeur-1")
        println("SUCCÈS : Écriture réussie (Le quorum de 2 est atteint !)")
        println("Donnée : ${mapProtegee.get("cle-1")}")
    } catch (e: Exception) {
        println("ÉCHEC : L'écriture a échoué (Quorum non atteint). Erreur : ${e.message}")
    }
    //hazelcastInstance1.shutdown()
    //println("Instance arrêtée. Programme terminé.")
}

