package adventOfCode2020

import org.junit.Test

class Day2 {
    @Test fun testDayTwo() {
        val testData = getFileAsListOfLines("/day2")
        val passing = testData.filter { s -> passwordMatch(s.split(" ")) }
        assert(passing.size == 378)
    }

    @Test fun testDayTwoPartTwo() {
        val testData = getFileAsListOfLines("/day2")
        val passing = testData.filter { s -> passwordMatchPartTwo(s.split(" ")) }
        assert(passing.size == 280)
    }

    private fun passwordMatch(values: List<String>) : Boolean {
        val low = values[0].split("-")[0].toInt()
        val high = values[0].split("-")[1].toInt()
        val letter = values[1].split(":")[0][0]
        val password = values[2]
        val occurrences = password.count { it == letter }

        return occurrences in low..high
    }

    private fun passwordMatchPartTwo(values: List<String>) : Boolean {
        val low = values[0].split("-")[0].toInt() - 1
        val high = values[0].split("-")[1].toInt() - 1
        val letter = values[1].split(":")[0][0]
        val password = values[2]

        return (password[low] == letter && password[high] != letter) || (password[low] != letter && password[high] == letter)
    }
}