package adventOfCode2020

import kotlin.test.Test

fun String.distinctChars(): List<Char> = this.toCharArray().distinct()
fun String.intersect(other: Iterable<Char>): Set<Char> = this.toCharArray().intersect(other)
fun List<String>.distinctChars() = this.joinToString("").distinctChars()

class Day6 {
    @Test
    fun testDay6() {
        val data = listOfListsByBlankLine(getFileAsListOfLines("/day6"))

        assert(countUnique(data) == 7110)
        assert(countUnique2(data) == 3628)
    }

    private fun countUnique(data: List<List<String>>) = data.map { i -> i.distinctChars().size }.sum()

    private fun countUnique2(data: List<List<String>>) = data.map { i ->
        i.fold(
            i.distinctChars().toSet()
        ) { acc, item -> item.intersect(acc) }.size
    }.sum()
}