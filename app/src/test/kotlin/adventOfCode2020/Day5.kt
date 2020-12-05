package adventOfCode2020

import kotlin.math.roundToInt
import kotlin.test.Test

class Day5 {
    @Test
    fun testDay5() {
        val testData: List<String> = getFileAsListOfLines("/day5")

        assert(seatId("BFFFBBFRRR") == 567)
        assert(seatId("FFFBBBFRRR") == 119)
        assert(seatId("BBFFBBFRLL") == 820)
        assert(seatId("FBFBBFFRLR") == 357)

        val seatIds = testData.map { seatId(it) }
        val maxSeatId = seatIds.max()
        assert(maxSeatId == 861)
        val remaining =  (0..861).map{ i -> if (seatIds.contains(i)) 0 else i }.filter { it != 0 }
        assert(remaining.max() == 633)
    }

    private fun seatId(s: String): Int {
        return (s.substring(0,7).fold(Pair(0,127)) { acc, item -> seat(item, acc) }.first) * 8 +
                s.substring(7).fold(Pair(0,7)) { acc, item -> seat(item, acc) }.first
    }

    private fun seat(l: Char, p: Pair<Int, Int>): Pair<Int, Int> = if (l == 'L' || l == 'F') low(p) else high(p)

    private fun low(p:Pair<Int,Int>) : Pair<Int, Int> {
        val gap = (((p.second.toDouble()) - (p.first.toDouble()))/ 2.0).roundToInt()
        return Pair(p.first, p.second - gap)
    }

    private fun high(p:Pair<Int,Int>) : Pair<Int, Int> {
        val gap = (((p.second.toDouble()) - (p.first.toDouble()))/ 2.0).roundToInt()
        return Pair(p.first + gap, p.second)
    }
}