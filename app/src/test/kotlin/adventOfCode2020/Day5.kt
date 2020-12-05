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
        val fb = s.substring(0,7)
        var rowPair = Pair(0,127)
        var row = -1

        for(i in fb) {
            rowPair = row(i, rowPair)
        }

        row = if (fb[6] == 'F') rowPair.first else rowPair.second

        val lr = s.substring(7, s.length)
        var colPair = Pair(0,7)
        var col = -1

        for(i in lr) {
            colPair = col(i, colPair)
        }

        col = if (lr[2] == 'L') colPair.first else colPair.second

        return row * 8 + col
    }

    private fun row(l: Char, current: Pair<Int, Int>): Pair<Int, Int> {
        if (l == 'F') {
            val gap = (((current.second.toDouble()) - (current.first.toDouble()))/ 2.0).roundToInt()
            return Pair(current.first, current.second - gap)
        }
        else {
            val gap = (((current.second.toDouble()) - (current.first.toDouble()))/ 2.0).roundToInt()
            return Pair(current.first + gap, current.second)
        }
    }

    private fun col(l: Char, current: Pair<Int, Int>): Pair<Int, Int> {
        if (l == 'L') {
            val gap = (((current.second.toDouble()) - (current.first.toDouble()))/ 2.0).roundToInt()
            return Pair(current.first, current.second - gap)
        }
        else {
            val gap = (((current.second.toDouble()) - (current.first.toDouble()))/ 2.0).roundToInt()
            return Pair(current.first + gap, current.second)
        }
    }
}