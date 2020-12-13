package adventOfCode2020

import org.junit.Test
import java.lang.Long.max
import kotlin.math.floor


class Day13 {

    // Part 1

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

    // Part 2

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day13")
        val buses = lines[1].split(",").mapIndexed{ index, b -> Pair(index, b) }
            .filter { it.second != "x" }.map { p -> Pair(p.first, p.second.toLong()) }
        val timestamp = busesLineUpPrimeIds(buses)

        assert(timestamp == 939490236001473)
    }

    @Test
    fun testPart2test() {
        val lines: List<String> = getFileAsListOfLines("/day13test")
        val buses = lines[1].split(",").mapIndexed{ index, b -> Pair(index, b) }
            .filter { it.second != "x" }.map { p -> Pair(p.first, p.second.toLong()) }
        val timestamp = busesLineUpPrimeIds(buses)

        assert(timestamp == 1068781L)
    }

    private fun busesLineUpPrimeIds(l: List<Pair<Int, Long>>) : Long {
        val stopDivisor = l.map { it.second }.fold(1L){acc, x -> acc * x}
        val startTime = l.map { it.second }.maxOrNull()!!.toLong()

        return atTimestamp(startTime, l, stopDivisor)
    }

    private tailrec fun atTimestamp(time: Long, l: List<Pair<Int, Long>>, lcm: Long): Long {
        val divisor = lookForLCMWithPrimes(time, 1, l)

        return if (divisor == lcm)
            time
        else
            atTimestamp(time + divisor, l, lcm)
    }

    private tailrec fun lookForLCMWithPrimes(time: Long, step: Long, l: List<Pair<Int, Long>>): Long {
        return when {
            l.isEmpty() -> return step
            (time + l.first().first) % l.first().second != 0L -> step
            else -> lookForLCMWithPrimes(time, max(step, step * l.first().second), l.drop(1))
        }
    }

    // Part 2 all number

    @Test
    fun testPart2AllNum() {
        val lines: List<String> = getFileAsListOfLines("/day13")
        val buses = lines[1].split(",").mapIndexed{ index, b -> Pair(index, b) }
            .filter { it.second != "x" }.map { p -> Pair(p.first, p.second.toLong()) }
        val timestamp = busesLineUpAllNum(buses)

        assert(timestamp == 939490236001473)
    }

    @Test
    fun testPart2testAllNum() {
        val lines: List<String> = getFileAsListOfLines("/day13test")
        val buses = lines[1].split(",").mapIndexed{ index, b -> Pair(index, b) }
            .filter { it.second != "x" }.map { p -> Pair(p.first, p.second.toLong()) }
        val timestamp = busesLineUpAllNum(buses)

        assert(timestamp == 1068781L)
    }

    private fun busesLineUpAllNum(l: List<Pair<Int, Long>>) : Long {
        val stopDivisor = l.map { it.second }.fold(1L){acc, x -> acc * x}
        val startTime = l.map { it.second }.maxOrNull()!!.toLong()

        return atTimestampAllNum(startTime, l, stopDivisor)
    }

    private tailrec fun atTimestampAllNum(time: Long, l: List<Pair<Int, Long>>, stopDivisor: Long): Long {
        val step = lookForLCMAllNum(time, listOf(1), l)

        return if (step == -1L)
            time
        else
            atTimestampAllNum(time + step, l, stopDivisor)
    }
    private tailrec fun lookForLCMAllNum(time: Long, matched: List<Long>, l: List<Pair<Int, Long>>): Long {
        return when {
            l.isEmpty() -> return -1
            (time + l.first().first) % l.first().second != 0L -> lcm(matched)
            else -> lookForLCMAllNum(time, matched + l.first().second, l.drop(1))
        }
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    private fun gcd(numbers: List<Long>): Long = numbers.fold(0L) { x, y -> gcd(x, y) }

    private fun lcm(numbers: List<Long>): Long = numbers.fold(1L) { x, y -> x * (y / gcd(x, y)) }
}