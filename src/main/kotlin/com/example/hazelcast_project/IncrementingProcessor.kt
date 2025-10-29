package com.example.hazelcast_project

import com.hazelcast.map.EntryProcessor
import java.io.Serializable
import java.util.Map

class IncrementingProcessor : EntryProcessor<String, Int, Int>, Serializable {
    override fun process(entry: MutableMap.MutableEntry<String, Int>): Int {
        val oldValue = entry.value ?: 0
        val newValue = oldValue + 1
        entry.setValue(newValue)
        return newValue
    }
}