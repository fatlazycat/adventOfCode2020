package adventOfCode2020

import kotlin.test.Test

class Day6 {
    @Test
    fun testDay6() {
        val data = dataSet(getFileAsListOfLines("/day6"))

        assert(countUnique(data) == 7110)
        assert(countUnique2(data) == 3628)
    }

    private fun countUnique(data: List<List<String>>) =
        data.map { i -> i.joinToString("").toCharArray().distinct().size }.sum()

    private fun countUnique2(data: List<List<String>>) = data.map { i ->
        i.drop(1).fold(i[0].toCharArray().toSet()) { acc, item -> item.toCharArray().intersect(acc) }.size
    }.sum()
}