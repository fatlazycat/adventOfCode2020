package adventOfCode2020

import org.junit.Test

class Day1 {
    @Test fun testDayOne() {
        val lessThan2020 = TestData.day1.filter { it < 2020 }
        val matches = matchesd1p1(lessThan2020)
        assert(matches == listOf(Pair(321, 1699), Pair(1699, 321)))
    }

    @Test fun testDayOnePartTwo() {
        val lessThan2020 = TestData.day1.filter { it < 2020 }
        val result = triples(lessThan2020)
        result?.let { t -> assert(t.first * t.second * t.third == 257778836) }
    }

    private fun matchesd1p1(values: List<Int>) : List<Pair<Int,Int>> {
        return values.map { v -> sumTo2020(v, values.filter { it != v }) }.flatten()
    }

    private fun sumTo2020(left: Int, right: List<Int>): List<Pair<Int,Int>> {
        return right.map{ Pair(left, it) }.filter{ p -> p.first + p.second == 2020 }
    }

    private fun triples(l: List<Int>) : Triple<Int, Int, Int>? {
        for (first in l) {
            for(second in l) {
                for(third in l) {
                    if(first != second && second != third && first != third && (first + second + third) == 2020) {
                        return Triple(first, second, third)
                    }
                }
            }
        }

        return null
    }
}