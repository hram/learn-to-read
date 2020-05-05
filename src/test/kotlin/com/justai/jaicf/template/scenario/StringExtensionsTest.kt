package com.justai.jaicf.template.scenario

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class StringExtensionsTest {

    @Test
    fun md5() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", "".md5())
        assertEquals("6e6296289c0195898548e75ee0ec4bdf", "макароны".md5())
        assertEquals("1a2219e58bd381970f3b8f41b4703b56", "попреблагорассмотрительствующемуся".md5())
    }
}