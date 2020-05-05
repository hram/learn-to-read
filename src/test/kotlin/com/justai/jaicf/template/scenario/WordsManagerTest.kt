package com.justai.jaicf.template.scenario

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class WordsManagerTest {

    private val manager = WordsManager()

    @Test
    fun getWordsByLenght() {

        val words = manager.getWords(9)

        assertTrue(words.size == 1)

        words[0].apply {
            assertEquals("some_word", _id)
            assertEquals("some_word", word)
            assertEquals(9, length)
        }
    }

    @Test
    fun getWordById() {
        val word = manager.getWord("some_word")
        assertEquals("some_word", word._id)
        assertEquals("some_word", word.word)
        assertEquals(9, word.length)
    }

    @Test
    fun getFirstWord() {
        val word = manager.getFirstWord(2)
        assertEquals("не", word._id)
        assertEquals("не", word.word)
        assertEquals(2, word.length)
    }

    @Test
    fun insertOne() {
        val item = WordModel(_id = "test_word", index = 0, word = "test_word", length = "test_word".length, imageUrl = "test_url")
        manager.insertOne(item)
    }

    @Test
    fun replaceOne() {
        val item = WordModel(_id = "test_word", index = 0, word = "test_word2", length = "test_word2".length, imageUrl = "test_url")
        manager.replaceOne(item)
    }
}