package adventOfCode2020

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.plus
import org.junit.Test

class Day24 {
    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day24")
        val data = lines.map { processRow(it) }
        val totalBlacks = keepBlacks(data).size

        assert(totalBlacks == 300)
    }

    @Test
    fun testPart1test() {
        val lines: List<String> = getFileAsListOfLines("/day24test")
        val data = lines.map { processRow(it) }
        val totalBlacks = keepBlacks(data).size

        assert(totalBlacks == 10)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day24")
        val data = lines.map { processRow(it) }
        var pattern = keepBlacks(data).toMap()

        repeat(100) {
            pattern = processDay(pattern)
        }

        assert(pattern.size == 3466)
    }

    @Test
    fun testPart2testSanity() {
        val lines: List<String> = getFileAsListOfLines("/day24test")
        val data = lines.map { processRow(it) }
        val initialPattern = keepBlacks(data)
        val afterDay1 = processDay(initialPattern)
        val afterDay2 = processDay(afterDay1)
        val afterDay3 = processDay(afterDay2)

        assert(afterDay1.size == 15)
        assert(afterDay2.size == 12)
        assert(afterDay3.size == 25)
    }

    @Test
    fun testPart2test() {
        val lines: List<String> = getFileAsListOfLines("/day24test")
        val data = lines.map { processRow(it) }
        var pattern = keepBlacks(data).toMap()

        repeat(100) {
            pattern = processDay(pattern)
        }

        assert(pattern.size == 2208)
    }

    private fun processDay(data: Map<Pair<Int, Int>, Boolean>): Map<Pair<Int, Int>, Boolean> {
        val bounds = getBounds(data)
        val min = Pair(bounds.first.first - 2, bounds.first.second - 1)
        val max = Pair(bounds.second.first + 2, bounds.second.second + 1)

        return (min.second..max.second).map { y ->
            val offset = if (y % 2 == 0) 0 else 1// we have a black tile

            // we have a white tile
            ((min.first - offset)..(max.first + offset) step 2).mapNotNull { x ->
                val coord = Pair(x, y)
                val adjacent = adjacentBlackTiles(coord, data)
                val current = data[coord]

                if (current == null) {
                    // we have a white tile
                    if (adjacent == 2)
                        coord to true
                    else
                        null
                } else {
                    // we have a black tile
                    if (adjacent == 0 || adjacent > 2)
                        null
                    else
                        coord to true
                }
            }
        }.flatten().toMap()
    }

    private fun adjacentBlackTiles(coord: Pair<Int, Int>, data: Map<Pair<Int, Int>, Boolean>): Int {
        val ne = if(data[Pair(coord.first+1, coord.second+1)] != null) 1 else 0
        val nw = if(data[Pair(coord.first-1, coord.second+1)] != null) 1 else 0
        val se = if(data[Pair(coord.first+1, coord.second-1)] != null) 1 else 0
        val sw = if(data[Pair(coord.first-1, coord.second-1)] != null) 1 else 0
        val e = if(data[Pair(coord.first+2, coord.second)] != null) 1 else 0
        val w = if(data[Pair(coord.first-2, coord.second)] != null) 1 else 0

        return ne + nw + se + sw + e + w
    }

    private fun getBounds(data: Map<Pair<Int, Int>, Boolean>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var min = data.keys.fold(Pair(0,0)){ acc, p ->
            Pair(Integer.min(acc.first, p.first), Integer.min(acc.second, p.second))
        }

        var max = data.keys.fold(Pair(1, 1)){ acc, p ->
            Pair(Integer.max(acc.first, p.first), Integer.max(acc.second, p.second))
        }

        if(min.first % 2 != 0)
            min = Pair(min.first-1, min.second)
        if(max.first % 2 != 0)
            max = Pair(max.first+1, max.second)

        return Pair(min, max)
    }


    private tailrec fun keepBlacks(data: List<List<Direction>>,
                                   current: PersistentMap<Pair<Int, Int>, Boolean> = persistentMapOf())
    : PersistentMap<Pair<Int, Int>, Boolean> {
        return if(data.isEmpty())
            return current
        else {
            val coord = getCoords(data.first())
            val tile = current[coord]

            if(tile == null) {
                // was white make black
                keepBlacks(data.drop(1), current + (coord to true))
            }
            else {
                // was black make white
                keepBlacks(data.drop(1), current.remove(coord))
            }
        }
    }

    private tailrec fun getCoords(data: List<Direction>, current: Pair<Int, Int> = Pair(0,0)): Pair<Int, Int> {
        return if(data.isEmpty())
            current
        else
            when(data.first()) {
                Direction.NW -> getCoords(data.drop(1), Pair(current.first-1, current.second+1))
                Direction.NE -> getCoords(data.drop(1), Pair(current.first+1, current.second+1))
                Direction.SW -> getCoords(data.drop(1), Pair(current.first-1, current.second-1))
                Direction.SE -> getCoords(data.drop(1), Pair(current.first+1, current.second-1))
                Direction.E -> getCoords(data.drop(1), Pair(current.first+2, current.second))
                Direction.W -> getCoords(data.drop(1), Pair(current.first-2, current.second))
            }
    }

    private tailrec fun processRow(s: String, current: List<Direction> = listOf()): List<Direction> {
        return if (s.isEmpty())
            current
        else {
            when {
                s.startsWith("se") -> processRow(s.drop(2), current + Direction.SE)
                s.startsWith("sw") -> processRow(s.drop(2), current + Direction.SW)
                s.startsWith("ne") -> processRow(s.drop(2), current + Direction.NE)
                s.startsWith("nw") -> processRow(s.drop(2), current + Direction.NW)
                s.startsWith("e") -> processRow(s.drop(1), current + Direction.E)
                s.startsWith("w") -> processRow(s.drop(1), current + Direction.W)
                else -> throw UnsupportedOperationException()
            }
        }
    }

    private enum class Direction {
        E,
        SE,
        SW,
        W,
        NW,
        NE
    }
}