package adventOfCode2020

import org.junit.Test

class Day23 {
    private val puzzleInput = "962713854"
    private val puzzleInputTest = "389125467"

    @Test
    fun testPart1test() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()

        println(numbers)
    }

    private fun processGame(numbers: List<Int>, roundsToGo: Int): List<String> {
        if(roundsToGo == 0) {
            return numbers
        }
        else {
            val currentCup = numbers.first()
            val remainingCups = numbers.drop(1)
            val threeCups = remainingCups.take(3)
            val allCupsWithoutThree = currentCup + remainingCups.drop(3)
            val index = findInsertionPoint(allCupsWithoutThree, currentCup)
        }
    }

    private fun findInsertionPoint(allCupsWithoutThree: List<Int>, currentCup: Int): Int {
        val nextCup = when(currentCup) {
            1 -> 9
            else -> currentCup - 1
        }
    }
}