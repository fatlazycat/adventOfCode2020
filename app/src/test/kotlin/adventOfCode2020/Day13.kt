package adventOfCode2020

import org.junit.Test
import java.lang.Long.max
import kotlin.math.floor


class Day13 {
    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day13")
        val timestamp = lines[0].toInt()
        val buses = lines[1].split(",").filter { it != "x" }.map { it.toInt() }
        val bus = nearestBus(timestamp, buses)
        val result = bus.first * bus.second

        assert(result == 246)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day13")
        val buses = lines[1].split(",").mapIndexed{ index, b -> Pair(index, b) }
            .filter { it.second != "x" }.map { p -> Pair(p.first, p.second.toLong()) }
        val timestamp = busesLineUp(buses)

        assert(timestamp == 939490236001473)
    }

    @Test
    fun testPart2test() {
        val lines: List<String> = getFileAsListOfLines("/day13test")
        val buses = lines[1].split(",").mapIndexed{ index, b -> Pair(index, b) }
            .filter { it.second != "x" }.map { p -> Pair(p.first, p.second.toLong()) }
        val timestamp = busesLineUp(buses)

        assert(timestamp == 1068781L)
    }

    private fun busesLineUp(l: List<Pair<Int, Long>>) : Long {
        val stopDivisor = l.map { it.second }.fold(1L){acc, x -> acc * x}
        val startTime = l.map { it.second }.maxOrNull()!!.toLong()

        return atTimestamp(startTime, l, stopDivisor)
    }

    private tailrec fun atTimestamp(time: Long, l: List<Pair<Int, Long>>, stopDivisor: Long): Long {
        val divisor = lookForMultiplier(time, 1, l)

        return if (divisor == stopDivisor)
            time
        else
            atTimestamp(time + divisor, l, stopDivisor)
    }

    private tailrec fun lookForMultiplier(time: Long, step: Long, l: List<Pair<Int, Long>>): Long {
        return when {
            l.isEmpty() -> return step
            (time + l.first().first) % l.first().second != 0L -> step
            else -> lookForMultiplier(time, max(step, step * l.first().second), l.drop(1))
        }
    }

    private fun nearestLowMultiple(x: Double, m: Double) = if (m == 0.0) x else floor(x / m) * m

    private fun nearestBus(timestamp: Int, buses: List<Int>): Pair<Int, Int> {
        return buses.map { m ->
                val nearest = nearestLowMultiple(timestamp.toDouble(), m.toDouble()).toInt()

                if (nearest != timestamp)
                    Pair(m, (nearest + m) - timestamp)
                else
                    Pair(m, 0)
            }.minByOrNull { it.second }!!
    }
}