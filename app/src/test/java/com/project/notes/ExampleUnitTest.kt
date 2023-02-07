package com.project.notes

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


        private fun regexTest(title: String): Boolean {
            val pattern = Regex("^[a-zA-Z0-9@$-]*$")
            return pattern.matches(title)
        }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun regexSpaceText() {
        val result = regexTest(" 54354")
        assertEquals(false, result)
    }
}