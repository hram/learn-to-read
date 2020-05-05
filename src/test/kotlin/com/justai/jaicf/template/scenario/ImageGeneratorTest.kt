package com.justai.jaicf.template.scenario

import org.junit.jupiter.api.Test
import java.io.File

class ImageGeneratorTest {

    private val manager = WordsManager()

    @Test
    fun generateOne() {
        var count = 0
        val generator = ImageGenerator(388, 172)
        var prev: WordModel? = null
        File("litw-win.txt").forEachLine {
            val res = it.split(" ")
            val word = res[res.size - 1]
            if (word.length == 1 && count < 10) {
                prev?.apply {
                    manager.replaceOne(WordModel(this.word, this.index, this.word, this.word.length, this.imageUrl, word))
                }
                prev = WordModel(word, count, word, word.length, generator.drawString(word.toUpperCase()))
                count++
            }
        }

        prev?.apply {
            manager.replaceOne(WordModel(this.word, this.index, this.word, this.word.length, this.imageUrl))
        }
    }
}