package com.example.hazelcast_project

import com.hazelcast.map.MapStore
import java.io.Serializable
import java.sql.Connection
import java.sql.DriverManager

class PersonMapStore : MapStore<String, Person>, Serializable {

    private val dbConnection: Connection

    init {
        println("[MapStore] Initialisation de la base de données H2...")
        dbConnection = DriverManager.getConnection("jdbc:h2:mem:hazelcast-db;DB_CLOSE_DELAY=-1", "sa", "")
        
        dbConnection.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS persons (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(255),
                age INT,
                city VARCHAR(255)
            )
            """
        )
        println("[MapStore] Table 'persons' prête.")
    }

    override fun store(key: String, value: Person) {
        println("[MapStore] STORE: Clé=$key, Valeur=$value")
        try {
            val stmt = dbConnection.prepareStatement("MERGE INTO persons VALUES (?, ?, ?, ?)")
            stmt.setString(1, key)
            stmt.setString(2, value.name)
            stmt.setInt(3, value.age)
            stmt.setString(4, value.city)
            stmt.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun load(key: String): Person? {
        println("[MapStore] LOAD: Clé=$key (Recherche en BDD...)")
        try {
            val stmt = dbConnection.prepareStatement("SELECT * FROM persons WHERE id = ?")
            stmt.setString(1, key)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                println("[MapStore] LOAD: Donnée trouvée pour la clé $key")
                return Person(
                    name = rs.getString("name"),
                    age = rs.getInt("age"),
                    city = rs.getString("city")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        println("[MapStore] LOAD: Aucune donnée trouvée pour la clé $key")
        return null 
    }


    override fun storeAll(map: Map<String, Person>) {
        map.forEach { (key, value) -> store(key, value) }
    }

    override fun loadAll(keys: Collection<String>): Map<String, Person> {
        return keys.mapNotNull { key -> load(key)?.let { value -> key to value } }.toMap()
    }

    override fun delete(key: String) {
        println("[MapStore] DELETE: Clé=$key")
        try {
            val stmt = dbConnection.prepareStatement("DELETE FROM persons WHERE id = ?")
            stmt.setString(1, key)
            stmt.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deleteAll(keys: Collection<String>) {
        keys.forEach { key -> delete(key) }
    }

    override fun loadAllKeys(): Iterable<String> {
        println("[MapStore] LOAD ALL KEYS")
        val keys = mutableListOf<String>()
        try {
            val stmt = dbConnection.prepareStatement("SELECT id FROM persons")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                keys.add(rs.getString("id"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return keys
    }
}