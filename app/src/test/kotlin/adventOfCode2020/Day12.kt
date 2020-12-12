package adventOfCode2020

import org.junit.Test
import kotlin.math.absoluteValue

class Day12 {
    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day12")
        val directions = mapData(lines)
        val result = followPath(directions)

        assert(result == 820)
    }

    @Test
    fun testChangeDirection() {
        val current = Path(Pair(0,0), Direction.EAST)
        assert( changeDirection(current, Pair(Direction.LEFT, 90)) == Path(Pair(0,0), Direction.NORTH))
        assert( changeDirection(current, Pair(Direction.LEFT, 180)) == Path(Pair(0,0), Direction.WEST))
        assert( changeDirection(current, Pair(Direction.LEFT, 360)) == Path(Pair(0,0), Direction.EAST))
        assert( changeDirection(current, Pair(Direction.RIGHT, 90)) == Path(Pair(0,0), Direction.SOUTH))
        assert( changeDirection(current, Pair(Direction.RIGHT, 180)) == Path(Pair(0,0), Direction.WEST))
        assert( changeDirection(current, Pair(Direction.RIGHT, 360)) == Path(Pair(0,0), Direction.EAST))
    }

    private fun followPath(l: List<Pair<Direction, Int>>) : Int {
        val newPosition = l.fold(Path(Pair(0,0), Direction.EAST)){ acc, i ->
            when(i.first) {
                Direction.NORTH -> Path(Pair(acc.current.first + i.second, acc.current.second), acc.direction)
                Direction.SOUTH -> Path(Pair(acc.current.first - i.second, acc.current.second), acc.direction)
                Direction.EAST -> Path(Pair(acc.current.first, acc.current.second + i.second), acc.direction)
                Direction.WEST -> Path(Pair(acc.current.first, acc.current.second - i.second), acc.direction)
                Direction.LEFT -> changeDirection(acc, i)
                Direction.RIGHT -> changeDirection(acc, i)
                Direction.FORWARD -> moveForward(acc, i)
                else -> throw UnsupportedOperationException()
            }
        }

        return newPosition.current.first.absoluteValue + newPosition.current.second.absoluteValue
    }

    private fun moveForward(current: Path, instruction: Pair<Direction, Int>): Path {
        val newPosition = when(current.direction) {
            Direction.NORTH -> Path(Pair(current.current.first + instruction.second, current.current.second), current.direction)
            Direction.EAST -> Path(Pair(current.current.first, current.current.second + instruction.second), current.direction)
            Direction.SOUTH -> Path(Pair(current.current.first - instruction.second, current.current.second), current.direction)
            Direction.WEST -> Path(Pair(current.current.first, current.current.second - instruction.second), current.direction)
            else -> throw UnsupportedOperationException()
        }

        return newPosition
    }

    private fun changeDirection(current: Path, instruction: Pair<Direction, Int>): Path {
        val turns = instruction.second / 90
        val newDirection = when(instruction.first) {
            Direction.LEFT -> Direction.from((4 + current.direction.num - turns) % 4)
            Direction.RIGHT -> Direction.from((current.direction.num + turns) % 4)
            else -> throw UnsupportedOperationException()
        }

        return Path(current.current, newDirection)
    }

    private fun mapData(l: List<String>) : List<Pair<Direction, Int>> {
        return l.map { i ->
            val direction = when(i[0]) {
                'N' -> Direction.NORTH
                'S' -> Direction.SOUTH
                'E' -> Direction.EAST
                'W' -> Direction.WEST
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                'F' -> Direction.FORWARD
                else -> Direction.UNKNOWN
            }

            Pair(direction, i.substring(1).toInt())
        }
    }

    private data class Path(val current: Pair<Int, Int>, val direction: Direction)

    enum class Direction(val num: Int) {
        NORTH(0),
        SOUTH(2),
        EAST(1),
        WEST(3),
        LEFT(4),
        RIGHT(5),
        FORWARD(6),
        UNKNOWN(7);

        companion object {
            fun from(v: Int): Direction = Direction.values().first { it.num == v }
        }
    }
}