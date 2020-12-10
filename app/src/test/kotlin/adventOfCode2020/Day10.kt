package adventOfCode2020

import org.junit.Test

class Day10 {
    @Test
    fun testPart1() {
        val lines = getFileAsListOfLines("/day10")
        val data = lines.map { it.toInt() }
        val sortedData = data.sorted()
        val d = findChain(sortedData + listOf(sortedData.maxOrNull()!! + 3))

        assert(d.one * d.three == 1998)
    }

    @Test
    fun testPart2() {
        val lines = getFileAsListOfLines("/day10")
        val data = lines.map { it.toInt() }
        val sortedData = data.sorted()
        val total = getCombinations(sortedData + (sortedData.maxOrNull()!! + 3))

        assert(total == 347250213298688L)
    }

    private fun getCombinations(l: List<Int>): Long {
        return l.fold(listOf(Pair(0, 1L))) { acc, i ->
            val possibleCombinations = acc.sumOf { p -> if(i - p.first <=3) p.second else 0L }
            acc.takeLast(2) + Pair(i, possibleCombinations)
        }.last().second
    }


    private fun findChain(l: List<Int>): Data {
        return l.fold(Data(0, 0, 0, 0)) { acc, i ->
            processEntry(i, acc)
        }
    }

    private fun processEntry(i: Int, acc: Data): Data {
        return when (i) {
            acc.last + 1 -> Data(i, acc.one + 1, acc.two, acc.three)
            acc.last + 2 -> Data(i, acc.one, acc.two + 1, acc.three)
            acc.last + 3 -> Data(i, acc.one, acc.two, acc.three + 1)
            else -> Data(-1, -1, -1, -1)
        }
    }

    private data class Data(val last: Int, val one: Int, val two: Int, val three: Int)
}

