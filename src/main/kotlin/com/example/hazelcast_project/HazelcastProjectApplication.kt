package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.config.Config
import com.hazelcast.config.MapConfig
import com.hazelcast.config.MapStoreConfig
import com.example.hazelcast_project.PersonMapStore
import com.example.hazelcast_project.Person

fun main(args: Array<String>) = runBlocking {
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val tacheAFaire: IQueue<Int> = hazelcastInstance1.getQueue("tâche-à-faire")
    val resultat:IMap<Int, String> = hazelcastInstance1.getMap("resultats")
    launch {
        for i in (1..20) {
            tacheAFaire.add(i)
        }
    }
}

