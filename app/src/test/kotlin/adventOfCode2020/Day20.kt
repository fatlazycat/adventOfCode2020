package adventOfCode2020

import org.junit.Test
import java.lang.UnsupportedOperationException

class Day20 {
    private enum class Side {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    @Test
    fun testPart1() {
        val data = listOfListsByBlankLine(getFileAsListOfLines("/day20"))
        val grids = processIntoGrids(data)
        val result = findPattern(grids)
        assert(result == 8272903687921)
    }

    private fun findPattern(grids: Map<Long, Array<CharArray>>): Long {
        val corners = grids.filter { g -> matchesForGrid(g.key, g.value, grids) == 2 }

        return corners.keys.fold(1){ acc, i ->
            acc * i
        }
    }

//    private fun traverse(gridKey: Long, grid: Array<CharArray>, grids: Map<Long, Array<CharArray>>, gridSize: Int, results: List<Long>): List<Long> {
//        return if(results.size == grids.size) {
//            results
//        } else {
//            val match = matchesForGridSingle(gridKey, grid, grids)
//
//            if(match == null)
//                return results
//            else
//                traverse(match.first, match.second, grids, gridSize, results + match.first)
//        }
//    }

    private fun matchesForGrid(gridKey: Long, grid: Array<CharArray>, grids: Map<Long, Array<CharArray>>): Int {
        return allGridOptions(grid).map { g1 ->
            findMatches(Side.LEFT, Side.RIGHT, gridKey, g1, grids.toList()).size +
                    findMatches(Side.RIGHT, Side.LEFT, gridKey, g1, grids.toList()).size +
                    findMatches(Side.TOP, Side.BOTTOM, gridKey, g1, grids.toList()).size +
                    findMatches(Side.BOTTOM, Side.TOP, gridKey, g1, grids.toList()).size
        }.maxOrNull()!!
    }

//    private fun matchesForGridSingle(gridKey: Long, grid: Array<CharArray>, grids: Map<Long, Array<CharArray>>): Pair<Long, Array<CharArray>>? {
//        val r = allGridOptions(grid).map { g1 ->
//            findMatches(Side.LEFT, Side.RIGHT, gridKey, g1, grids.toList())
//        }.flatten()
//
//        if(r.size > 1) {
//            throw UnsupportedOperationException()
//        }
//        else if (r.size == 1){
//            return r[0]
//        }
//        else {
//            return null
//        }
//
//    }

    private fun findMatches(
        g1S: Side,
        g2S: Side,
        currentKey: Long,
        grid: Array<CharArray>,
        grids: List<Pair<Long, Array<CharArray>>>
    ): List<Pair<Long, Array<CharArray>>> {
        return grids.map { p ->
            if (p.first != currentKey) {
                val options = allGridOptions(p.second)
                val matchedOptions = options.filter { matchEdges(grid, g1S, it, g2S) }
                matchedOptions.map { p.first to it }
            } else {
                listOf()
            }
        }.flatten()
    }

    private fun matchEdges(g1: Array<CharArray>, g1S: Side, g2: Array<CharArray>, g2S: Side): Boolean {
        val g1Pattern: CharArray = when (g1S) {
            Side.TOP -> g1[9]
            Side.BOTTOM -> g1[0]
            Side.RIGHT -> g1.map { it[9] }.toCharArray()
            Side.LEFT -> g1.map { it[0] }.toCharArray()
        }

        val g2Pattern: CharArray = when (g2S) {
            Side.TOP -> g2[9]
            Side.BOTTOM -> g2[0]
            Side.RIGHT -> g2.map { it[9] }.toCharArray()
            Side.LEFT -> g2.map { it[0] }.toCharArray()
        }

        return g1Pattern.contentEquals(g2Pattern)
    }

    private fun allGridOptions(grid: Array<CharArray>): List<Array<CharArray>> {
        val g0 = grid
        val g90 = rotateGrid90DegreesCounterClockwise(g0)
        val g180 = rotateGrid90DegreesCounterClockwise(g90)
        val g270 = rotateGrid90DegreesCounterClockwise(g180)
        val gf0 = flipGridInHorizontal(grid)
        val gf90 = rotateGrid90DegreesCounterClockwise(gf0)
        val gf180 = rotateGrid90DegreesCounterClockwise(gf90)
        val gf270 = rotateGrid90DegreesCounterClockwise(gf180)

        return listOf(
            g0, g90, g180, g270,
            gf0, gf90, gf180, gf270
        )
    }

    private fun processIntoGrids(data: List<List<String>>): Map<Long, Array<CharArray>> {
        return data.map { l ->
            val gridNumber = l[1].substring(0, 4).toLong()
            val gridData = l.drop(2).reversed()
            val grid = gridData.map { it.toCharArray() }.toTypedArray()

            gridNumber to grid
        }.toMap()
    }

    private fun flipGridInHorizontal(grid: Array<CharArray>): Array<CharArray> {
        return grid.reversed().toTypedArray()
    }

    private fun rotateGrid90DegreesCounterClockwise(grid: Array<CharArray>): Array<CharArray> {
        val result = Array(10) { CharArray(10) { ' ' } }

        for (x in 0..9) {
            for (y in 0..9) {
                result[y][9 - x] = grid[x][y]
            }
        }

        return result
    }

    @Test
    fun testFlipGrid() {
        val sut = flipGridInHorizontal(getTestGrid(testGrid))
        val expected = getTestGrid(flippedTestGrid)

        assert(sut.contentDeepEquals(expected))
    }

    @Test
    fun testRotateGrid() {
        val sut = rotateGrid90DegreesCounterClockwise(getTestGrid(testGridToRotate))
        val expected = getTestGrid(testGridTRotated)

        assert(sut.contentDeepEquals(expected))
    }

    private fun getTestGrid(s: String): Array<CharArray> {
        return s.lines().reversed().map { it.toCharArray() }.toTypedArray()
    }

    private val testGrid = """
        ..##.#..#.
        ##..#.....
        #...##..#.
        ####.#...#
        ##.##.###.
        ##...#.###
        .#.#.#..##
        ..#....#..
        ###...#.#.
        ..###..###
    """.trimIndent()

    private val flippedTestGrid = """
        ..###..###
        ###...#.#.
        ..#....#..
        .#.#.#..##
        ##...#.###
        ##.##.###.
        ####.#...#
        #...##..#.
        ##..#.....
        ..##.#..#.
    """.trimIndent()

    private val testGridToRotate = """
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        .........#
    """.trimIndent()

    private val testGridTRotated = """
        .........#
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
    """.trimIndent()
}