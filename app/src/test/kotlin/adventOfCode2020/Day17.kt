package adventOfCode2020

import org.junit.Test
import java.lang.Integer.max
import java.lang.Integer.min

class Day17 {
    private val testData = listOf(".#.", "..#", "###")
    private val data = listOf(
        "#.##....",
        ".#.#.##.",
        "###.....",
        "....##.#",
        "#....###",
        ".#.#.#..",
        ".##...##",
        "#..#.###")

    @Test
    fun testPart1() {
        val grid = processInputData(data,3)
        val finalGrid = process(grid, 6, 3)
        assert(finalGrid.keys.size == 230)
    }

    @Test
    fun testPart1TestData() {
        val grid = processInputData(testData,3)
        val finalGrid = process(grid, 6, 3)
        assert(finalGrid.keys.size == 112)
    }

    @Test
    fun testPart2() {
        val grid = processInputData(data, 4)
        val finalGrid = process(grid, 6, 4)
        assert(finalGrid.keys.size == 1600)
    }

    @Test
    fun testPart2TestData() {
        val grid = processInputData(testData, 4)
        val finalGrid = process(grid, 6, 4)
        assert(finalGrid.keys.size == 848)
    }

    private tailrec fun process(states: Map<Point, Boolean>, tries: Int, numDimensions: Int): Map<Point, Boolean> {
        return if (tries == 0) {
            states
        } else {
            val bounds = getBounds(states, numDimensions)
            val newGrid = processAllPossibleSites(states, bounds.first, bounds.second, Point(), 0, numDimensions).toMap()
            process(newGrid, tries-1, numDimensions)
        }
    }

    private fun processAllPossibleSites(states: Map<Point, Boolean>, min: Point, max: Point, pointToCheck: Point, dimension: Int, numDimensions: Int) : List<Pair<Point, Boolean>> {
        return if (dimension == numDimensions)
            listOfNotNull(stateChange(pointToCheck, states, numDimensions))
        else {
            (min.d[dimension]-1 .. max.d[dimension]+1).map { c ->
                processAllPossibleSites(states, min, max, Point(*pointToCheck.d, c), dimension+1, numDimensions)
            }.flatten()
        }
    }

    private fun stateChange(p: Point, states: Map<Point, Boolean>, numDimensions: Int): Pair<Point, Boolean>? {
        val state = states[p]
        val active = adjacent(p, states, Point(), 0, numDimensions)

        return if(state == null ) {
            if(active == 3)
                Pair(p, true)
            else
                null
        } else {
            if(active == 2 || active == 3)
                Pair(p, true)
            else
                null
        }
    }

    private fun getBounds(states: Map<Point, Boolean>, numDimensions: Int): Pair<Point, Point> {
        val max = states.keys.fold(Point(*generateSequence { 1 }.take(numDimensions).toList().toIntArray())){ acc, p ->
            Point(*(0 until numDimensions).map { i -> max(p.d[i], acc.d[i]) }.toIntArray())
        }

        val min = states.keys.fold(Point(*generateSequence { 0 }.take(numDimensions).toList().toIntArray())){ acc, p ->
            Point(*(0 until numDimensions).map { i -> min(p.d[i], acc.d[i]) }.toIntArray())
        }

        return Pair(min, max)
    }

    private fun adjacent(p: Point, states: Map<Point, Boolean>, pointToCheck: Point, dimension: Int, numDimensions: Int): Int {
        return if (dimension == numDimensions) {
            if(p != pointToCheck)
                checkState(states, pointToCheck)
            else
                0
        } else {
            (p.d[dimension]-1 .. p.d[dimension]+1).map { c ->
                adjacent(p, states, Point(*pointToCheck.d, c), dimension+1, numDimensions)
            }.sum()
        }
    }

    private fun checkState(
        states: Map<Point, Boolean>,
        point: Point
    ) = when (states[point]) {
        null -> 0
        else -> 1
    }

    private fun processInputData(lines: List<String>, numDimensions: Int): Map<Point, Boolean> {
        return lines.mapIndexed { x, row ->
            row.mapIndexed { y, state ->
                if(state == '#') {
                    val twoDim = intArrayOf(x, y)
                    val zeros = generateSequence { 0 }.take(numDimensions - 2).toList().toIntArray()
                    Point(*twoDim, *zeros) to true
                }
                else
                    null
            }.filterNotNull()
        }.flatten().toMap()
    }

    private class Point(vararg val d: Int) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Point

            if (!d.contentEquals(other.d)) return false

            return true
        }

        override fun hashCode(): Int {
            return d.contentHashCode()
        }

        override fun toString(): String {
            return "PointMulti(d=${d.contentToString()})"
        }
    }
}