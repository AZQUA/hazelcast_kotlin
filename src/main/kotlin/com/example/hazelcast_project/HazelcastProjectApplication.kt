package com.example.hazelcast_project

import com.hazelcast.core.Hazelcast
import com.hazelcast.map.IMap
import com.hazelcast.collection.IList
import com.hazelcast.collection.ISet
import com.hazelcast.collection.IQueue
import kotlinx.coroutines.*
import com.hazelcast.core.EntryListener
import com.hazelcast.core.EntryAdapter
import com.hazelcast.core.EntryEvent 


fun main(args: Array<String>) {
    println("Démarrage...")
    val hazelcastInstance1 = Hazelcast.newHazelcastInstance()
    val mapListener: IMap<String, String> = hazelcastInstance1.getMap("ma-map")
    mapListener.addEntryListener(object : EntryAdapter<String,String>() {
        override fun entryAdded(event: EntryEvent<String, String>) {
            val timestampActuel = java.util.Date()
            println("Listener Donnée AJOUTEE : Clé=${event.key}, Valeur=${event.value}, Date=${timestampActuel}")
        }
        override fun entryUpdated(event: EntryEvent<String, String>) {
            val timestampActuel = java.util.Date()
            println("LISTENER Donnée MISE A JOUR : Clé=${event.key}, NouvelleValeur=${event.value}, Date=${timestampActuel}")
        }
        override fun entryRemoved(event: EntryEvent<String, String>) {
            val timestampActuel = java.util.Date()
            println("LISTENER Donnée SUPPRIMEE : Clé=${event.key}, Date=${timestampActuel}")
        }
    }, true)

    mapListener.put("cle-1","donnee-1")
    mapListener.put("cle-1","donnee-1-modif")
    mapListener.remove("cle-1")
}