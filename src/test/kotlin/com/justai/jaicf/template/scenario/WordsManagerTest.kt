package com.justai.jaicf.template.scenario

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class WordsManagerTest {

    val login = "alice"

    val password = "9RS400dV"

    private val uri1 = "mongodb://$login:$password@cluster0-agj1r.mongodb.net/test"

    private val uri2 = "mongodb://$login:$password@ds055762.mlab.com:55762/heroku_9w2h7mvv"

    private val uri = MongoClientURI(uri2)

    private val client = MongoClient(uri)

    private val manager = WordsManager(client.getDatabase(uri.database!!).getCollection("words"))

    @Test
    fun getWords() {

        val words = manager.getWords(1234)

        assertTrue(words.size == 1)
    }
}