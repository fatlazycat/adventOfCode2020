package adventOfCode2020

import org.junit.Test
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