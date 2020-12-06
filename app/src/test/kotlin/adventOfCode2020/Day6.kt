package adventOfCode2020

import kotlin.test.Test

class Day6 {
    @Test
    fun testDay6() {
        val data = listOfListsByBlankLine(getFileAsListOfLines("/day6"))

        assert(countUnique(data) == 7110)
        assert(countUnique2(data) == 3628)
    }

    private fun countUnique(data: List<List<String>>) =
        data.map { i -> distinctChars(i).size }.sum()

    private fun countUnique2(data: List<List<String>>) = data.map { i ->
        i.fold(distinctChars(i).toSet()) { acc, item -> item.toCharArray().intersect(acc) }.size
    }.sum()

    private fun distinctChars(s: List<String>) = s.joinToString("").toCharArray().distinct()
}