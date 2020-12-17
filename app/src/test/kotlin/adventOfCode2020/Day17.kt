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
        val grid = processData(data)
        val finalGrid = process(grid, 6)
        assert(finalGrid.keys.size == 230)
    }

    @Test
    fun testPart1TestData() {
        val grid = processData(testData)
        val finalGrid = process(grid, 6)
        assert(finalGrid.keys.size == 112)
    }

    @Test
    fun testPart2() {
        val grid = processDataHyper(data)
        val finalGrid = processHyper(grid, 6)
        assert(finalGrid.keys.size == 1600)
    }

    @Test
    fun testPart2TestData() {
        val grid = processDataHyper(testData)
        val finalGrid = processHyper(grid, 6)
        assert(finalGrid.keys.size == 848)
    }

    private tailrec fun process(states: Map<Point, Boolean>, tries: Int): Map<Point, Boolean> {
        return if (tries == 0) {
            states
        } else {
            val bounds = getBounds(states)
            val newGrid = processAllPossibleSites(states, bounds.first, bounds.second)
            process(newGrid, tries-1)
        }
    }

    private tailrec fun processHyper(states: Map<PointHyper, Boolean>, tries: Int): Map<PointHyper, Boolean> {
        return if (tries == 0) {
            states
        } else {
            val bounds = getBoundsHyper(states)
            val newGrid = processAllPossibleSitesHyper(states, bounds.first, bounds.second)
            processHyper(newGrid, tries-1)
        }
    }

    private fun processAllPossibleSites(states: Map<Point, Boolean>, min: Point, max: Point) : Map<Point, Boolean> {
        return (min.x - 1 .. max.x + 1).map { xc ->
            (min.y - 1 .. max.y + 1).map { yc ->
                (min.z - 1 .. max.z + 1).map { zc ->
                    stateChange(Point(xc, yc, zc), states)
                }.filterNotNull()
            }.flatten()
        }.flatten().toMap()
    }

    private fun processAllPossibleSitesHyper(states: Map<PointHyper, Boolean>, min: PointHyper, max: PointHyper) : Map<PointHyper, Boolean> {
        return (min.x - 1 .. max.x + 1).map { xc ->
            (min.y - 1 .. max.y + 1).map { yc ->
                (min.z - 1 .. max.z + 1).map { zc ->
                    (min.w - 1..max.w + 1).map { wc ->
                        stateChangeHyper(PointHyper(xc, yc, zc, wc), states)
                    }.filterNotNull()
                }.flatten()
            }.flatten()
        }.flatten().toMap()
    }

    private fun stateChange(p: Point, states: Map<Point, Boolean>): Pair<Point, Boolean>? {
        val state = states[p]
        val active = adjacent(p.x, p.y, p.z, states)

        return if(state == null ) {
            if(active == 3)
                Pair(Point(p.x,p.y,p.z), true)
            else
                null
        } else {
            if(active == 2 || active == 3)
                Pair(Point(p.x,p.y,p.z), true)
            else
                null
        }
    }

    private fun stateChangeHyper(p: PointHyper, states: Map<PointHyper, Boolean>): Pair<PointHyper, Boolean>? {
        val state = states[p]
        val active = adjacentHyper(p.x, p.y, p.z, p.w, states)

        return if(state == null ) {
            if(active == 3)
                Pair(PointHyper(p.x,p.y,p.z,p.w), true)
            else
                null
        } else {
            if(active == 2 || active == 3)
                Pair(PointHyper(p.x,p.y,p.z,p.w), true)
            else
                null
        }
    }

    private fun getBounds(states: Map<Point, Boolean>): Pair<Point, Point> {
        val max = states.keys.fold(Point(1,1,1)){ acc, p ->
            val newX = max(p.x, acc.x)
            val newY = max(p.y, acc.y)
            val newZ = max(p.z, acc.z)
            Point(newX, newY, newZ)
        }

        val min = states.keys.fold(Point(0,0,0)){ acc, p ->
            val newX = min(p.x, acc.x)
            val newY = min(p.y, acc.y)
            val newZ = min(p.z, acc.z)
            Point(newX, newY, newZ)
        }

        return Pair(min, max)
    }

    private fun getBoundsHyper(states: Map<PointHyper, Boolean>): Pair<PointHyper, PointHyper> {
        val max = states.keys.fold(PointHyper(1,1,1,1)){ acc, p ->
            val newX = max(p.x, acc.x)
            val newY = max(p.y, acc.y)
            val newZ = max(p.z, acc.z)
            val newW = max(p.w, acc.w)
            PointHyper(newX, newY, newZ, newW)
        }

        val min = states.keys.fold(PointHyper(0,0,0,0)){ acc, p ->
            val newX = min(p.x, acc.x)
            val newY = min(p.y, acc.y)
            val newZ = min(p.z, acc.z)
            val newW = min(p.w, acc.w)
            PointHyper(newX, newY, newZ, newW)
        }

        return Pair(min, max)
    }

    @Test
    fun testAdjacent() {
        val total = adjacent(-1, -1, -1, listOf(Point(0,0,0) to true).toMap())
        assert(total == 1)
    }

    private fun adjacent(x: Int, y: Int, z: Int, states: Map<Point, Boolean>): Int {
        return (x - 1..x + 1).map { xc ->
            (y - 1..y + 1).map { yc ->
                (z - 1..z + 1).map { zc ->
                    if (x != xc || y != yc || z != zc)
                        checkState(states, xc, yc, zc)
                    else
                        0
                }.sum()
            }.sum()
        }.sum()
    }

    private fun adjacentHyper(x: Int, y: Int, z: Int, w: Int, states: Map<PointHyper, Boolean>): Int {
        return (x - 1..x + 1).map { xc ->
            (y - 1..y + 1).map { yc ->
                (z - 1..z + 1).map { zc ->
                    (w - 1..w + 1).map { wc ->
                    if (x != xc || y != yc || z != zc || w != wc)
                        checkStateHyper(states, xc, yc, zc, wc)
                    else
                        0
                    }.sum()
                }.sum()
            }.sum()
        }.sum()
    }

    private fun checkState(
        states: Map<Point, Boolean>,
        x: Int,
        y: Int,
        z: Int
    ) = when (states[Point(x,y,z)]) {
        null -> 0
        else -> 1
    }

    private fun checkStateHyper(
        states: Map<PointHyper, Boolean>,
        x: Int,
        y: Int,
        z: Int,
        w: Int
    ) = when (states[PointHyper(x,y,z,w)]) {
        null -> 0
        else -> 1
    }

    private fun processData(lines: List<String>): Map<Point, Boolean> {
        return lines.mapIndexed { x, row ->
            row.mapIndexed { y, state ->
                if(state == '#')
                    Point(x,y,0) to true
                else
                    null
            }.filterNotNull()
        }.flatten().toMap()
    }

    private fun processDataHyper(lines: List<String>): Map<PointHyper, Boolean> {
        return lines.mapIndexed { x, row ->
            row.mapIndexed { y, state ->
                if(state == '#')
                    PointHyper(x,y,0,0) to true
                else
                    null
            }.filterNotNull()
        }.flatten().toMap()
    }

    private data class Point(val x: Int, val y: Int, val z: Int)
    private data class PointHyper(val x: Int, val y: Int, val z: Int, val w:Int)
}