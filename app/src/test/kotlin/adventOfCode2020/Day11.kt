package adventOfCode2020

import org.junit.Test

class Day11 {
    enum class Position(val pos: Int) {
        EMPTY(0),
        FULL(1),
        FLOOR(2),
        UNKNOWN(3)
    }

    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day11")
        val seats =
            lines.mapIndexed { i, row -> row.mapIndexed { j, seat -> Pair(i, j) to seatType(seat) } }.flatten().toMap()
        val processedSeats = processToEnd(seats)
        val occupied = processedSeats.values.filter { it == Position.FULL }.count()

        assert(occupied == 2204)
    }

    private fun processToEnd(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        var current = seats

        do {
            val stepOne = seatsToOccupied(current)

            if(stepOne == current)
                return current
            else
                current = stepOne

            val stepTwo = seatsToUnoccupied(current)

            if(stepTwo == current)
                return current
            else
                current = stepTwo

        } while (true)
    }


    private fun seatsToOccupied(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        return seats.keys.map { k ->
            val currentType = seats[k]!!

            if (currentType != Position.FLOOR) {
                val adjacent = adjacent(k.first, k.second, seats)
                if (adjacent == 0)
                    k to Position.FULL
                else
                    k to currentType
            } else {
                k to currentType
            }
        }.toMap()
    }

    private fun seatsToUnoccupied(seats: Map<Pair<Int, Int>, Position>): Map<Pair<Int, Int>, Position> {
        return seats.keys.map { k ->
            val currentType = seats[k]!!

            if (currentType != Position.FLOOR) {
                val adjacent = adjacent(k.first, k.second, seats)
                if (adjacent >= 4)
                    k to Position.EMPTY
                else
                    k to currentType
            } else {
                k to currentType
            }
        }.toMap()
    }

    private fun adjacent(initialRow: Int, initialCol: Int, seats: Map<Pair<Int, Int>, Position>): Int {
        var full = 0

        (initialRow - 1..initialRow + 1).map { row ->
            (initialCol - 1..initialCol + 1).map { col ->
                if (row != initialRow || col != initialCol) {
                    val otherSeat = seats[Pair(row, col)]
                    if(otherSeat != null && otherSeat == Position.FULL)
                        full++
                }
            }
        }

        return full
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