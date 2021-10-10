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
        val result = countCorners(grids)
        assert(result == 8272903687921)
    }

    @Test
    fun testPart2test() {
        val data = listOfListsByBlankLine(getFileAsListOfLines("/day20test"))
        val grids = processIntoGrids(data)
        val allPossibleGrids = getAllPossibleAnswers(grids, 3)
        val withoutBorders = removeBorders(allPossibleGrids)
        val combinedGrids = asOneGrid(withoutBorders, 3)
        val allPossibleBigGrids = combinedGrids.map { m -> allGridOptions(m) }.flatten()
        val results = allPossibleBigGrids.map { findSeaMonsters(it, 3) }.filter { it != 0 }.distinct()

        assert(results.size == 1)
        assert(results[0] == 273)
    }

    @Test
    fun testPart2() {
        val data = listOfListsByBlankLine(getFileAsListOfLines("/day20"))
        val grids = processIntoGrids(data)
        val allPossibleGrids = getAllPossibleAnswers(grids, 12)
        val withoutBorders = removeBorders(allPossibleGrids)
        val combinedGrids = asOneGrid(withoutBorders, 12)
        val allPossibleBigGrids = combinedGrids.map { m -> allGridOptions(m) }.flatten()
        val results = allPossibleBigGrids.map { findSeaMonsters(it, 12) }.filter { it != 0 }.distinct()

        assert(results.size == 1)
        assert(results[0] == 2304)
    }

    @Test
    fun testResultGrid() {
        val grid = getGridFromVariable(resultGrid)
        val result = findSeaMonsters(grid, 12)

        assert(result == 2304)
    }

    private fun findSeaMonsters(grid: Array<CharArray>, gridSize: Int): Int {
        val seaMonster = getGridFromVariable(seaMonster)
        var numSeaMonsters = 0
        val outputGrid = grid.copyOf()

        for (x in 0..grid.size - 3) {
            for (y in 0..grid.size - 20) {
                var found = true

                for (i in 0..2) {
                    for (j in 0..19) {
                        if (seaMonster[i][j] == '#' && grid[x + i][y + j] != '#')
                            found = false
                    }
                }

                if (found) {
                    numSeaMonsters += 1
                    for (i in 0..2) {
                        for (j in 0..19) {
                            if (seaMonster[i][j] == '#')
                                outputGrid[x + i][y + j] = 'O'
                        }
                    }
                }
            }
        }

        var count = 0
        var monsters = 0

        if (numSeaMonsters > 0) {
            for (x in 0 until grid.size) {
                for (y in 0 until grid.size) {
                    if (outputGrid[x][y] == '#')
                        count += 1
                    else if (outputGrid[x][y] == 'O')
                        monsters += 1
                }
            }
        }

        return count
    }

    private fun asOneGrid(
        allSolutions: List<List<Pair<Long, Array<CharArray>>>>,
        gridSize: Int
    ): List<Array<CharArray>> {
        val r = allSolutions.map { solution ->
            val result = Array(8 * gridSize) { CharArray(8 * gridSize) { ' ' } }

            for (x in 0 until gridSize) {
                for (y in 0 until gridSize) {
                    val grid = solution[(x * gridSize) + y].second

                    for (i in 0..7) {
                        for (j in 0..7) {
                            result[(x * 8) + i][(y * 8) + j] = grid[i][j]
                        }
                    }
                }
            }

            result
        }

        return r
    }

    private fun getAllPossibleAnswers(
        grids: Map<Long, Array<CharArray>>,
        gridSize: Int
    ): List<List<Pair<Long, Array<CharArray>>>> {
        val corners = grids.filter { g -> matchesForGrid(g.key, g.value, grids) == 2 }

        val r = corners.flatMap { corner ->
            allGridOptions(corner.value).map { co ->
                val q = traverse(corner.key, co, grids, listOf(Pair(corner.key, co)), Pair(corner.key, co), gridSize)
                q
            }.filter { it.size == grids.size }
        }

        return r
    }

    private fun traverse(
        gridKey: Long,
        grid: Array<CharArray>,
        grids: Map<Long, Array<CharArray>>,
        results: List<Pair<Long, Array<CharArray>>>,
        lastFirstOrRow: Pair<Long, Array<CharArray>>,
        gridSize: Int
    ): List<Pair<Long, Array<CharArray>>> {

        return if (results.size == grids.size) {
            results
        } else {
            if (results.size % gridSize == 0) {
                val topMatch = matchesForGridSingleTop(lastFirstOrRow.first, lastFirstOrRow.second, grids)

                if (topMatch == null)
                    results
                else
                    traverse(topMatch.first, topMatch.second, grids, results + topMatch, topMatch, gridSize)

            } else {
                val rightMatch = matchesForGridSingleRight(gridKey, grid, grids)

                if (rightMatch == null)
                    results
                else
                    traverse(rightMatch.first, rightMatch.second, grids, results + rightMatch, lastFirstOrRow, gridSize)
            }
        }
    }

    private fun removeBorders(allSolutions: List<List<Pair<Long, Array<CharArray>>>>): List<List<Pair<Long, Array<CharArray>>>> {
        val r = allSolutions.map { solution ->
            solution.map { gridPair ->
                val grid = gridPair.second
                val removeTopAndBottom = grid.drop(1).dropLast(1)
                val allRemoved = removeTopAndBottom.map { ca -> ca.drop(1).dropLast(1).toCharArray() }.toTypedArray()

                gridPair.first to allRemoved
            }
        }

        return r
    }

    @Test
    fun testRemoveBorder() {
        val expected = getGridFromVariable(minusBorderExample)
        val data = listOf(listOf(1L to getGridFromVariable(borderExample)))
        val result = removeBorders(data)
        assert(result.size == 1)
        assert(result.first().first().second.contentDeepEquals(expected))
    }


    private val borderExample = """
        ##########
        #........#
        #........#
        #........#
        #........#
        #........#
        #........#
        #........#
        #........#
        ##########
    """.trimIndent()

    private val minusBorderExample = """
        ........
        ........
        ........
        ........
        ........
        ........
        ........
        ........
    """.trimIndent()

    private fun countCorners(grids: Map<Long, Array<CharArray>>): Long {
        val corners = grids.filter { g -> matchesForGrid(g.key, g.value, grids) == 2 }

        return corners.keys.fold(1) { acc, i ->
            acc * i
        }
    }

    private fun matchesForGrid(gridKey: Long, grid: Array<CharArray>, grids: Map<Long, Array<CharArray>>): Int {
        val r = findMatches(Side.LEFT, Side.RIGHT, gridKey, grid, grids.toList()) +
                findMatches(Side.RIGHT, Side.LEFT, gridKey, grid, grids.toList()) +
                findMatches(Side.TOP, Side.BOTTOM, gridKey, grid, grids.toList()) +
                findMatches(Side.BOTTOM, Side.TOP, gridKey, grid, grids.toList())

        return r.distinctBy { it.first }.size
    }

    private fun matchesForGridSingleRight(
        gridKey: Long,
        grid: Array<CharArray>,
        grids: Map<Long, Array<CharArray>>
    ): Pair<Long, Array<CharArray>>? {
        val r = findMatches(Side.RIGHT, Side.LEFT, gridKey, grid, grids.toList()).distinctBy { it.first }

        return if (r.size > 1) {
            throw UnsupportedOperationException()
        } else if (r.size == 1) {
            r[0]
        } else {
            null
        }
    }

    private fun matchesForGridSingleTop(
        gridKey: Long,
        grid: Array<CharArray>,
        grids: Map<Long, Array<CharArray>>
    ): Pair<Long, Array<CharArray>>? {
        val r = findMatches(Side.TOP, Side.BOTTOM, gridKey, grid, grids.toList()).distinctBy { it.first }

        if (r.size > 1) {
            throw UnsupportedOperationException()
        } else if (r.size == 1) {
            return r[0]
        } else {
            return null
        }

    }

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
        val gfv0 = flipGridInVertical(grid)
        val gfv90 = rotateGrid90DegreesCounterClockwise(gfv0)
        val gfv180 = rotateGrid90DegreesCounterClockwise(gfv90)
        val gfv270 = rotateGrid90DegreesCounterClockwise(gfv180)

        return listOf(
            g0, g90, g180, g270,
            gf0, gf90, gf180, gf270,
            gfv0, gfv0, gfv180, gfv270
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

    private fun flipGridInVertical(grid: Array<CharArray>): Array<CharArray> {
        return grid.map { it.reversed().toCharArray() }.toTypedArray()
    }

    private fun rotateGrid90DegreesCounterClockwise(grid: Array<CharArray>): Array<CharArray> {
        val gridSize = grid.size
        val result = Array(gridSize) { CharArray(gridSize) { ' ' } }

        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                result[y][(gridSize - 1) - x] = grid[x][y]
            }
        }

        return result
    }

    @Test
    fun testFlipGrid() {
        val sut = flipGridInHorizontal(getGridFromVariable(testGrid))
        val expected = getGridFromVariable(flippedTestGrid)

        assert(sut.contentDeepEquals(expected))
    }

    @Test
    fun testRotateGrid() {
        val sut = rotateGrid90DegreesCounterClockwise(getGridFromVariable(testGridToRotate))
        val expected = getGridFromVariable(testGridRotated)

        assert(sut.contentDeepEquals(expected))
    }

    private fun getGridFromVariable(s: String): Array<CharArray> {
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

    private val testGridRotated = """
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

    private val seaMonster = """
    ..................#. 
    #....##....##....###
    .#..#..#..#..#..#...
    """.trimIndent()

    private val resultGrid = """
         ###....#..#..#.#.....#......#.#......#.....#....#....#.#...##...........##...##.#.........##..#.
         .##....#.....#..#.#.#.........#....####...#.........#.......##...#...##...#.#........#....#....#
         ...#....#.#.......##.....###......#..#..#......#....#....#...#..#..#....##.......#...#.#.#......
         ....##.#.##.#..#..........##..##.#.#.#.#..#.#..#....#...#...#..##..#.#..##.........#.#..#...#..#
         #.#.##.#...#......#.........#....##.#.#.#...#..#...#.....#.#....##.....###...#......#...#...##..
         #..#.....##.......#..#.......#......#.#......#.........##..#....#...##.....#....#...........##..
         .....................#...#.......#....#.##.#...#.....##.....##..#...#.#...#.........#.#.....#.##
         ..#...#....#..#..##..##..#....#...#.......##...##......#...#......#.#....#..#.......#.#..#.....#
         ...##.#......#...#...#.........#...#....##..##...##..#.####..#......##..#....#.#......#...##.#.#
         .......#.#...###....#.##..#..#...#..........#..#......##...##....###...###............#.#....#..
         #...#...###..####..#.###....#.......#.#.#......#..#....##.#..#####.##.#.##......##...##......#..
         .##.##..##.##.##.##.#......#.#....................#..##.#......#.#......#.......#..#..#...###...
         ...........#..#.##....#....#...##.#.#....#.#.#..#..#...#......##.#...#...##.#..#.#....#...##....
         ..#..#..#.#......#....#...#......##.......##.##.....#.#.#...#....#.#.#.#.#..........#...#.......
         #..#.........#.....#..#.....#..#...#..#......#..#.#..#..#...........##..##.....###...#..........
         ............##.#...#.........#.#..###.#.............#....#.#.....#.....#.#............#...######
         .....#.#..##.#...#..#.#.....#.#........##.#...#....#####..#...##.#..........#.....#.#..#........
         ..#.#..####.#..##..#.###..#..##....#.#..#..##...##..............#..##...#...........#...#.#....#
         .....#.##.##..#.######........#.....##.###.##..#....##.#...#.....#.##......##........#.##.......
         #.....#.#.#.#.....#.#.......#....#..#.#.....#..............#........##.#.##.#............##.#.##
         .......##...#.#...#..#.#......##.##.#.......#.###.###....#.#...........#............#..#.#....#.
         ###..#.#.#.#.#...#....#....#.#...#.....##................#......#..#.#...............#.......#..
         #....#.##.###...##..#.##....###..........#..####..#...............#......#.#####..#.#......#...#
         .#.....#....#..#.##..#..##.#......#.#####.....#..###..#.....##.#..#.#....#....###..........#####
         ....#..#......##...##...#.....#..#..#.####.###.#..###..#..#...#...#.....#.....#........#...#....
         ...........#.......##...##.#.#..#.##.####..#.##..#..#...#.....#.##.......##....#.......#.......#
         ...#........##........#.#..............##..#.....##...#.#.##.##....##.#.####.#....#..#.##.##...#
         #......##.....#.####.##..#.#.##....###.......#..#..#....##..#.##..#.##.##...........#..........#
         ...####.##....##..#.####....#....#.....#.#.#.#...#.#.##..####......................#.....#...#.#
         #...#..#..#..#.##..##........#..................#.#.......#..##...##.#...#.......##.....#.......
         #.##..#.............#....#.####...#..#.....#....#...#.......#.....#.#.........#.#..#....##...#.#
         ....#.####.#####..##......#..........#.#..#....###.#.##..#.###...#..........#.##..#....#.#....#.
         ##....#...#.#...........#..#....#......##..##.#..#..#..#..##....##.##.......##...##.....#...#...
         .#..####..#.....##..#...#.##............#............##..#...#.#..#..............#...#...#.###.#
         ....#....#..##.#.#..##.....#...##..#..#.#..####.......##..#..#...#..............##.....#...#..##
         ###...#...#..#...####...#..#..###.#............#.#...#..##......##................###..##..##...
         ...........##.....#...##....#....#.#.............#.#.#..#....#..........#...#...##..#....##..#.#
         .#.....##.....##......##......#.......##....##..##.#..#..#..........#.#.#..#.##.#.###....###....
         ......#........#..#.#..........##......#....#.....##.#.#..##.#...##...#..##.#..#..#.##..#..#.#.#
         .#......#.#.###.#.##......###.#.#...##..............###...#.#..#.#...#..#.#.......#..#.......#..
         ..##..#..........#...###.##...#.#..#.....#..........##......##.....#...#..#..#..#..##..#....#..#
         #.#......##.#..##.....###.........##..#..#...##..#...##.#..#..#.......##.#..#.......##..###.....
         #..#........#.#.##..#.#......#.#.......#.#.#..#.#......#..#.....#.....#...#...##...##.#..#...#..
         ...#..#..#.#.......##....##....##.....#.......#.#.#..##..........##......#.#...#...##.##.##.#...
         ....#....###.........##..........#...#.#....#..#....#.......###...#....#..#..##.......##.......#
         .....##..#..#...#...#.#...#.....##.....#..#..##........#.#...###..........#..##........#.#...#..
         .#..#...#..#...#..................#..#....##....##....###..#....#....#.....#...#..#..##.#..#.#..
         .#..#.###...##........................#..#.###.#..#..##.....#...........#.....###.......#...#...
         ##.........##.....#.#......#..##........#......##..................#.........#............#...#.
         .#.#...#.....#.#..#...#..#.#..##....#......#.....#...#.#..#..#.##........#......##.....##..#..#.
         #.#..#.....##.#.#..#.#.#..#.........##.#....#..##...##....#...#......#..#.............#...#.#...
         ###.#...##.#..........##..##..#..#...#.##.....##..###..###..#.##..#.###.#.......#..............#
         .......#..#......##...#..#.#....##.#.........####..###.#..#..#..####.#.....#...#..#....#.##.....
         ..#.......#.#.#..##...#.#..#.#.##.....#..#.....##..##..#.#.#.............#............#.#......#
         .....#.#........#....#.#.#.####..#.##.##..........##....#...#..#...#..##.#..#.........#......#..
         ....#.....#.....#.#...#.....#....#...#....##..#...#...................##.#........#...#.#.......
         #.#.#....#....##....###.#.##...####.#.....#....#...###.......##.#...###.....###..#.#.........#..
         ##.....##.#.##..#..#..#..#.##.##...#.......#.........##..#.#.#...#....#.#..#....#..#....#.......
         ...#.#.....###...##......#...#..#.#.......##......##...##.#..#..##...#.#..#.......#.##....#.....
         ....#....#.#...##.#..#.#....#.#........##..#.#......#..##....###......#.#..#.#.....##..#.......#
         .##.......#....#...#.....##...##.#...#.#..#.......#####......#.#...#....#..##.....#....#.....#..
         ##.##....#.................#.#.#.##.#.##.#.#.##.#...#..........#.#.......##......#.#..#.##....#.
         .##..........#.....#........#.......#........##...#......#....#..........#.....##.##..#..#......
         #..#....#.##.#...#.............##..#...#...##..#..#....#.#..#......#.##......#.#...#..........#.
         ...#.......#....#....#.####.#.###...##....####..#..#.##...............#.#......###.#...#.##.....
         #.................#.......#..#.##.##..##.##.........#..#...##..........#......#....#.#..#.......
         ......#....###.#...#..####.....#....#..#.#.#.......#...###......###......#......###..##.........
         ....##......#..####.#...#.#......####.....#.....#.##....#.#.#.........#.#..#.#......#........##.
         ..........#...#...#.##..#.....#.#.......#...#....#....#..###...#..#........#.....#.#..........#.
         ..#.##.....#......#..........##......###........#.....#..#.....#....#........#....#..........#.#
         .#.#......##....#.#..###.##..#..#..##....#..##.#...###..#..##.#.....#.##....#.#..#........#...##
         .#......###..###...###.#.####.#.#....#....#.###....#......#.##.........#...##.#....##..#...#...#
         #.....#..#..#..#.##..##.#............#......#...#..##......#.#....##.......#.........#.#.#..#...
         #..##....#...........#..#....##.#.#.#.#...#.#.......#..##..#.....##...##...#...##.###...#.....##
         #..##...#..#....#................#.......###..#.....#.......#...#.####.#........#...#..#.....##.
         ..#...........#.#.#..........#..#.#.#.#.#.#..#.##...#.#.....#.#.#....##.#.#..#.#..#..#..#...#...
         ...#.#...#..#......#...#....#.#...##.#.#....####.##..####.######...#...#.............#.#......#.
         ..........###..##....##....###.#.###.........####..#..#.###.###.#..............#............#...
         #...#...#..##.##.#.##..#..#............#...#...#...#....##...##.#....#.###..#........#....##....
         ##.......#..#.#.....###...#..#...........#####...#.#.........##...#.....#..#.#.#..#.##.....#....
         #.....#....#.#....#........#.....#..#.#..........##.#........#....##....##.....#.....#.#........
         .#.....#....#..#...#.........#....##.##...##...#....#....#...#...#..#.##.......#.#........##..#.
         .#....#..##..#...#####..##....##..#.####...#...###...#.#.#....#.............#.#..........#..##..
         .#.....##...........#..#..##.#.###.#.....##...........#....###..........##.#..#......#...##....#
         #...#..#.........#..###........##..#......#...#..##.###.###........#........#...#..##.#...#.#...
         .#....#.......#....####....##....#..#..#.....#....#......##.#....#.#..#......#..#...............
         ...#.#.............#.###.....#....................##.......##....#.#.........#.........#....##..
         ........#...#.#.....#.........#...#.#....#.........#....#..#.........#.....#.#.##....#.##.#.#..#
         ...............###....#......#.....##...#.#...#.#...#.........##..........###...#.###.....#...#.
         #.....#.#......#...#.#..#.........#....#..#..###.#.....#..............##...#...#....#...........
         .##.#.###...##....###...#..##.#..#.##............##.#.......#.........#.......###.......##....#.
         ..##.##.#..#..#..#..#..#.###............#...#.............###.......###...###..####..#.###....#.
         #.###...#......#........#.#.....#.....#.#.##....#...#......#.#........##..##.#.#####..#...#...#.
         ..............##..#...#..#....##.....#...#.........###.##.......#.#.##........##.#.#...#.#...#..
         #........#.........##....#.#....#..........##..#........#.###..##.##.........#.......##...#..#..
         #.....#.#..####.#.....#.#..#.#.#....##..#......#...#.#..####.......#.##.###.###.###..#.....#..#.
    """.trimIndent()
}