package adventOfCode2020

import org.junit.Test

// 2 variables
fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

class Day9 {
    @Test
    fun testPart1() {
        val lines = getFileAsListOfLines("/day9")
        val numberList = lines.map { it.toLong() }
        val answer = findInvalid(numberList, 25)
        assert(answer == 69316178L)
    }

    @Test
    fun testPart2() {
        val lines = getFileAsListOfLines("/day9")
        val numberList = lines.map { it.toLong() }
        val pair = findSum(numberList, 69316178L)
        val subList = numberList.subList(pair.first.toInt(), pair.second.toInt() - 1)
        val min = subList.minOrNull()
        val max = subList.maxOrNull()
        val answer = safeLet(min, max) { x, y ->
            x + y
        }

        assert(answer == 9351526L)
    }

    @Test
    fun testNoAnswerToBeFound() {
        assert(findSum((1L..4L).toList(), 11111) == Pair(-1L, -1L))
    }

    private fun findSum(l: List<Long>, n: Long): Pair<Long, Long> {
        if (l.isEmpty()) return Pair(-1L, -1)
        val indices = createPairs((1L until l.size).toList())
        val match = indices.find { i -> l.subList(i.first.toInt(), i.second.toInt()).sum() == n }

        return match ?: findSum(l.drop(1), n)
    }

    private fun findInvalid(l: List<Long>, n: Int): Long {
        val head = l.take(n)
        val tail = l.drop(n)
        val pairs = createPairs(head)
        val sumPairs = pairsSum(pairs)

        return if (!sumPairs.contains(tail.first()))
            tail.first()
        else
            findInvalid(l.drop(1), n)
    }

    private fun pairsSum(l: List<Pair<Long, Long>>): List<Long> {
        return l.map { i -> i.first + i.second }.distinct()
    }

    private fun createPairs(l: List<Long>): List<Pair<Long, Long>> {
        if (l.isEmpty()) return listOf()
        return l.drop(1).map { i -> Pair(l[0], i) } + createPairs(l.drop(1))
    }
}