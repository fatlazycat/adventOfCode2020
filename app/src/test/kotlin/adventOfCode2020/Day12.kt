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
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day12")
        val directions = mapData(lines)
        val result = followPathWaypoint(directions)

        assert(result == 66614)
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

    @Test
    fun testChangeDirectionWaypoint() {
        assert( changeDirectionWaypoint(PathWaypoint(Pair(0,0), Pair(4,10)), Pair(Direction.RIGHT, 90)) == PathWaypoint(Pair(0,0), Pair(-10,4)))
    }

    private fun followPath(l: List<Pair<Direction, Int>>) : Int {
        val newPosition = l.fold(Path(Pair(0,0), Direction.EAST)){ acc, i ->
            when(i.first) {
                Direction.NORTH -> Path(Pair(acc.position.first + i.second, acc.position.second), acc.direction)
                Direction.SOUTH -> Path(Pair(acc.position.first - i.second, acc.position.second), acc.direction)
                Direction.EAST -> Path(Pair(acc.position.first, acc.position.second + i.second), acc.direction)
                Direction.WEST -> Path(Pair(acc.position.first, acc.position.second - i.second), acc.direction)
                Direction.LEFT -> changeDirection(acc, i)
                Direction.RIGHT -> changeDirection(acc, i)
                Direction.FORWARD -> moveForward(acc, i)
                else -> throw UnsupportedOperationException()
            }
        }

        return newPosition.position.first.absoluteValue + newPosition.position.second.absoluteValue
    }

    private fun followPathWaypoint(l: List<Pair<Direction, Int>>) : Int {
        val newPosition = l.fold(PathWaypoint(Pair(0,0), Pair(1, 10))){ acc, i ->
            when(i.first) {
                Direction.NORTH -> PathWaypoint(acc.position, Pair(acc.waypoint.first + i.second, acc.waypoint.second))
                Direction.SOUTH -> PathWaypoint(acc.position, Pair(acc.waypoint.first - i.second, acc.waypoint.second))
                Direction.EAST -> PathWaypoint(acc.position, Pair(acc.waypoint.first, acc.waypoint.second + i.second))
                Direction.WEST -> PathWaypoint(acc.position, Pair(acc.waypoint.first, acc.waypoint.second - i.second))
                Direction.LEFT -> changeDirectionWaypoint(acc, i)
                Direction.RIGHT -> changeDirectionWaypoint(acc, i)
                Direction.FORWARD -> moveForwardWaypoint(acc, i)
                else -> throw UnsupportedOperationException()
            }
        }

        return newPosition.position.first.absoluteValue + newPosition.position.second.absoluteValue
    }

    private fun moveForward(current: Path, instruction: Pair<Direction, Int>): Path {
        return when (current.direction) {
            Direction.NORTH -> Path(
                Pair(current.position.first + instruction.second, current.position.second),
                current.direction
            )
            Direction.EAST -> Path(
                Pair(current.position.first, current.position.second + instruction.second),
                current.direction
            )
            Direction.SOUTH -> Path(
                Pair(current.position.first - instruction.second, current.position.second),
                current.direction
            )
            Direction.WEST -> Path(
                Pair(current.position.first, current.position.second - instruction.second),
                current.direction
            )
            else -> throw UnsupportedOperationException()
        }
    }

    private fun moveForwardWaypoint(current: PathWaypoint, instruction: Pair<Direction, Int>): PathWaypoint {
        return PathWaypoint(
            Pair(current.position.first + current.waypoint.first * instruction.second,
            current.position.second + current.waypoint.second * instruction.second),
            current.waypoint)
    }

    private fun changeDirection(position: Path, instruction: Pair<Direction, Int>): Path {
        val turns = instruction.second / 90
        val newDirection = when(instruction.first) {
            Direction.LEFT -> Direction.from((4 + position.direction.num - turns) % 4)
            Direction.RIGHT -> Direction.from((position.direction.num + turns) % 4)
            else -> throw UnsupportedOperationException()
        }

        return Path(position.position, newDirection)
    }

    private fun changeDirectionWaypoint(position: PathWaypoint, instruction: Pair<Direction, Int>): PathWaypoint {
        val turns = instruction.second / 90
        return when (Pair(turns, instruction.first)) {
            Pair(0, Direction.LEFT), Pair(0, Direction.RIGHT) ->
                PathWaypoint(
                    position.position,
                    position.waypoint)
            Pair(1, Direction.LEFT), Pair(3, Direction.RIGHT) ->
                PathWaypoint(
                    position.position,
                    Pair(position.waypoint.second, position.waypoint.first * -1)
                )
            Pair(2, Direction.LEFT), Pair(2, Direction.RIGHT) ->
                PathWaypoint(
                    position.position,
                    Pair(position.waypoint.first * -1, position.waypoint.second * -1)
                )
            Pair(3, Direction.LEFT), Pair(1, Direction.RIGHT) ->
                PathWaypoint(
                    position.position,
                    Pair(position.waypoint.second * -1, position.waypoint.first)
                )
            else -> throw UnsupportedOperationException()
        }
    }

    private fun mapData(l: List<String>) : List<Pair<Direction, Int>> = l.map { i ->
        Pair(
            Direction.fromLetter(i[0]),
            i.substring(1).toInt()
        )
    }

    private data class Path(val position: Pair<Int, Int>, val direction: Direction)

    private data class PathWaypoint(val position: Pair<Int, Int>, val waypoint: Pair<Int, Int>)

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
            fun from(v: Int): Direction = values().first { it.num == v }
            fun fromLetter(c: Char): Direction {
                return when(c) {
                    'N' -> NORTH
                    'S' -> SOUTH
                    'E' -> EAST
                    'W' -> WEST
                    'L' -> LEFT
                    'R' -> RIGHT
                    'F' -> FORWARD
                    else -> UNKNOWN
                }
            }
        }
    }
}