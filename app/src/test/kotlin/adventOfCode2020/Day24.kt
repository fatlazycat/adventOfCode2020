package adventOfCode2020

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.collections.immutable.*
import org.junit.Test
import java.lang.UnsupportedOperationException

class Day24 {
    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day24")
        val data = lines.map { processRow(it) }
        val totalBlacks = keepBlacks(data)

        assert(totalBlacks == 300)
    }

    @Test
    fun testPart1test() {
        val lines: List<String> = getFileAsListOfLines("/day24test")
        val data = lines.map { processRow(it) }
        val totalBlacks = keepBlacks(data)

        assert(totalBlacks == 10)
    }

    private tailrec fun keepBlacks(data: List<List<Direction>>, current: PersistentMap<Pair<Int, Int>, Boolean> = persistentMapOf()): Int {
        return if(data.isEmpty())
            return current.size
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