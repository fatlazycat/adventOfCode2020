package adventOfCode2020

import kotlin.test.Test

class Day6 {
    @Test
    fun testDay6() {
        val testData: List<String> = getFileAsListOfLines("/day6")

        assert(countUnique(testData) == 7110)
        assert(countUnique2(testData) == 3628)
    }

    private fun countUnique(testData: List<String>): Int {
        return dataSet(testData)
            .map { i -> i.joinToString("").toCharArray().distinct().size }.sum()
    }

    private fun countUnique2(testData: List<String>): Int {
        return dataSet(testData)
            .map { i ->
                i.drop(1).fold(i[0].toCharArray().toSet()) { acc, item -> item.toCharArray().intersect(acc) }.size
            }.sum()
    }
}