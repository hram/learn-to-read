package com.justai.jaicf.template.scenario

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RegexTest {

    private val regex = """(\d+) - (\d+)""".toRegex()

    @Test
    fun find() {
        var res = regex.find("1 - 5")
        assertEquals("1", res!!.groups[1]!!.value)
        assertEquals("5", res.groups[2]!!.value)

        res = regex.find("6 - 10")
        assertEquals("6", res!!.groups[1]!!.value)
        assertEquals("10", res.groups[2]!!.value)

        res = regex.find("11 - 15")
        assertEquals("11", res!!.groups[1]!!.value)
        assertEquals("15", res.groups[2]!!.value)
    }
}