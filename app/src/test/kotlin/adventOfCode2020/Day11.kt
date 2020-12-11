package adventOfCode2020

import org.junit.Test

class Day11 {

    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day11")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val processedSeats = processToEnd(seats, ::noAdjacentSeats, ::fourOrMoreFreeAdjacentSeats)
        val occupied = processedSeats.values.filter { it == Position.FULL }.count()

        assert(occupied == 2204)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day11")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val processedSeats = processToEnd(seats, ::noAdjacentSeatsVisible, ::fiveOrMoreFreeSeatsVisible)
        val occupied = processedSeats.values.filter { it == Position.FULL }.count()

        assert(occupied == 1986)
    }

    @Test
    fun testAdjacentSee1() {
        val lines: List<String> = getFileAsListOfLines("/day11test2")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val full = adjacentSee(4,3,seats)

        assert(full == 8)
    }

    @Test
    fun testAdjacentSee2() {
        val lines: List<String> = getFileAsListOfLines("/day11test3")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val full = adjacentSee(3,3,seats)

        assert(full == 0)
    }

    @Test
    fun testAdjacentSee3() {
        val lines: List<String> = getFileAsListOfLines("/day11test4")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val full = adjacentSee(1,1,seats)

        assert(full == 0)
    }

    enum class Position {
        EMPTY,
        FULL,
        FLOOR,
        UNKNOWN
    }

    private fun seatTest(
        p: Map.Entry<Pair<Int, Int>, Position>,
        seats: Map<Pair<Int, Int>, Position>,
        test: (Map.Entry<Pair<Int, Int>, Position>, Map<Pair<Int, Int>, Position>) -> Boolean,
        newType: Position
    ): Pair<Pair<Int, Int>, Position> {
        return if(p.value != Position.FLOOR && test(p, seats))
            p.key to newType
        else
            p.key to p.value
    }

    private fun noAdjacentSeats(p: Map.Entry<Pair<Int, Int>, Position>, seats: Map<Pair<Int, Int>, Position>): Pair<Pair<Int, Int>, Position> {
        return seatTest(p, seats, { e, s -> adjacent(e.key.first, e.key.second, s) == 0 }, Position.FULL)
    }

    private fun fourOrMoreFreeAdjacentSeats(p: Map.Entry<Pair<Int, Int>, Position>, seats: Map<Pair<Int, Int>, Position>): Pair<Pair<Int, Int>, Position> {
        return seatTest(p, seats, { e, s -> adjacent(e.key.first, e.key.second, s) >= 4 }, Position.EMPTY)
    }

    private fun noAdjacentSeatsVisible(p: Map.Entry<Pair<Int, Int>, Position>, seats: Map<Pair<Int, Int>, Position>): Pair<Pair<Int, Int>, Position> {
        return seatTest(p, seats, { e, s -> adjacentSee(e.key.first, e.key.second, s) == 0 }, Position.FULL)
    }

    private fun fiveOrMoreFreeSeatsVisible(p: Map.Entry<Pair<Int, Int>, Position>, seats: Map<Pair<Int, Int>, Position>): Pair<Pair<Int, Int>, Position> {
        return seatTest(p, seats, { e, s -> adjacentSee(e.key.first, e.key.second, s) >= 5 }, Position.EMPTY)
    }

    private fun processToEnd(seats: Map<Pair<Int, Int>, Position>,
                             seatTestToOccupy: (Map.Entry<Pair<Int, Int>, Position>, Map<Pair<Int, Int>, Position>) -> Pair<Pair<Int, Int>, Position> ,
                             seatTestToEmpty: (Map.Entry<Pair<Int, Int>, Position>, Map<Pair<Int, Int>, Position>) -> Pair<Pair<Int, Int>, Position> )
                             : Map<Pair<Int, Int>, Position> {
        var current = seats
        var firstFn = true

        do {
            val changedSeats = if(firstFn) seatsChange(current, seatTestToOccupy) else seatsChange(current, seatTestToEmpty)

            if(changedSeats == current)
                return current

            current = changedSeats
            firstFn = !firstFn

        } while (true)
    }

    private fun seatsChange(
        seats: Map<Pair<Int, Int>, Position>,
        seatTest: (Map.Entry<Pair<Int, Int>, Position>, Map<Pair<Int, Int>, Position>) -> Pair<Pair<Int, Int>, Position>
    ): Map<Pair<Int, Int>, Position> {
        return seats.entries.map { p: Map.Entry<Pair<Int, Int>, Position> ->
            seatTest(p, seats)
        }.toMap()
    }

    private fun adjacent(initialRow: Int, initialCol: Int, seats: Map<Pair<Int, Int>, Position>): Int {
        return (initialRow - 1..initialRow + 1).map { row ->
            (initialCol - 1..initialCol + 1).map { col ->
                if (initialRow != row || initialCol != col)
                    when(seats[Pair(row, col)]) {
                        Position.FULL -> 1
                        else -> 0
                    }
                else
                    0
            }.sum()
        }.sum()
    }

    private fun adjacentSee(initialRow: Int, initialCol: Int, seats: Map<Pair<Int, Int>, Position>): Int {
        return (-1..1).map { row ->
            (-1..1).map { col ->
                if (row != 0 || col != 0)
                    checkInOneDirection(seats, initialRow + row, initialCol + col, row, col)
                else
                    0
            }.sum()
        }.sum()
    }

    private tailrec fun checkInOneDirection(seats: Map<Pair<Int, Int>, Position>, initialRow: Int, initialCol: Int, row: Int, col: Int) : Int {
        return when (seats[Pair(initialRow, initialCol)]) {
            null -> 0
            Position.FULL -> 1
            Position.EMPTY -> 0
            else -> checkInOneDirection(seats, initialRow + row, initialCol + col, row, col)
        }
    }

    private fun seatType(c: Char): Position {
        return when (c) {
            '.' -> Position.FLOOR
            '#' -> Position.FULL
            'L' -> Position.EMPTY
            else -> Position.UNKNOWN
        }
    }
}