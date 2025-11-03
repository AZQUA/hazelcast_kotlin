package com.example.hazelcast_project

import java.io.Serializable

data class Person(
    val name: String,
    val age: Int,
    val city: String
) : Serializable