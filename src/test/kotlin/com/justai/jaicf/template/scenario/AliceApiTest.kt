package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.yandexalice.api.AliceApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class AliceApiTest {

    @Test
    fun uploadImage() {
        val imageP = "7cbc8ba6dbdd7f3ab825012faaffdace"
        val imageN = "8ca2ef7d6d4846de865c9de15d5a380e"
        val api = AliceApi("AgAAAAAAvk17AAT7o9bO4KEpbULckgyYDUCEk3w", "14ab4e6b-584b-494d-b4fb-e6802ca54f81")
        val image = api.uploadImage("http://hram0v.com/learn-to-read/$imageP.png")
        assertNotNull(image)
    }
}