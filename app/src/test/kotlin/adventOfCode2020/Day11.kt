package adventOfCode2020

import org.junit.Test

class Day11 {
    enum class Position {
        EMPTY,
        FULL,
        FLOOR,
        UNKNOWN
    }

    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day11")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val processedSeats = processToEnd(seats, ::seatsToOccupied, ::seatsToUnoccupied)
        val occupied = processedSeats.values.filter { it == Position.FULL }.count()

        assert(occupied == 2204)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day11")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val processedSeats = processToEnd(seats, ::seatsToOccupied2, ::seatsToUnoccupied2)
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

    private fun processToEnd(seats: Map<Pair<Int, Int>, Position>,
                             occFn: (seats: Map<Pair<Int, Int>, Position>) -> Map<Pair<Int, Int>, Position>,
                             unoccFn: (seats: Map<Pair<Int, Int>, Position>) -> Map<Pair<Int, Int>, Position>)
                             : Map<Pair<Int, Int>, Position> {
        var current = seats
        var firstFn = true

        do {
            val changedSeats = if(firstFn) occFn(current) else unoccFn(current)

            if(changedSeats == current)
                return current

            current = changedSeats
            firstFn = !firstFn

        } while (true)
    }

    private fun seatsToOccupied(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        return seats.entries.map { p: Map.Entry<Pair<Int, Int>, Position> ->
            if(p.value != Position.FLOOR && adjacent(p.key.first, p.key.second, seats) == 0 )
                p.key to Position.FULL
            else
                p.key to p.value

        }.toMap()
    }

    private fun seatsToUnoccupied(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        return seats.keys.map { k ->
            val currentType = seats[k]!!

            when {
                currentType != Position.FLOOR -> {
                    val adjacent = adjacent(k.first, k.second, seats)
                    if (adjacent >= 4)
                        k to Position.EMPTY
                    else
                        k to currentType
                }
                else -> {
                    k to currentType
                }
            }
        }.toMap()
    }

    private fun seatsToOccupied2(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        return seats.keys.map { k ->
            val currentType = seats[k]!!

            if (currentType != Position.FLOOR) {
                val adjacent = adjacentSee(k.first, k.second, seats)
                if (adjacent == 0)
                    k to Position.FULL
                else
                    k to currentType
            } else {
                k to currentType
            }
        }.toMap()
    }

    private fun seatsToUnoccupied2(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        return seats.keys.map { k ->
            val currentType = seats[k]!!

            if (currentType != Position.FLOOR) {
                val adjacent = adjacentSee(k.first, k.second, seats)
                if (adjacent >= 5)
                    k to Position.EMPTY
                else
                    k to currentType
            } else {
                k to currentType
            }
        }.toMap()
    }

    private fun adjacentSee(initialRow: Int, initialCol: Int, seats: Map<Pair<Int, Int>, Position>): Int {
        var full = 0

        (-1..1).map { row -> (-1..1).map { col ->
            if (row != 0 || col != 0) {
                var stop = false
                var checkRow = initialRow + row
                var checkCol = initialCol + col

                do {

                    when (seats[Pair(checkRow, checkCol)]) {
                        null -> stop = true
                        Position.FULL -> {
                            full++
                            stop = true
                        }
                        Position.EMPTY -> stop = true
                        else -> {
                            checkRow += row
                            checkCol += col
                        }
                    }
                }
                while(!stop)
            }
        }}

        return full
    }

    private fun adjacent(initialRow: Int, initialCol: Int, seats: Map<Pair<Int, Int>, Position>): Int {
        return (initialRow - 1..initialRow + 1).map { row ->
            (initialCol - 1..initialCol + 1).map { col ->
                val otherSeat = seats[Pair(row, col)]
                if ((row != initialRow || col != initialCol) && otherSeat != null && otherSeat == Position.FULL)
                    1
                else
                    0
            }
        }.flatten().sum()
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