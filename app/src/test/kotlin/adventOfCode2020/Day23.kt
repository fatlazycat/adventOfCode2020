package adventOfCode2020

import org.junit.Test

class Day23 {
    private val puzzleInput = "962713854"
    private val puzzleInputTest = "389125467"

    @Test
    fun testPart1test() {
        val numbers = puzzleInput.toCharArray().map { it.toString().toInt() }.toList()
        val result = getResult(processGame(numbers, 100))
        assert(result == "65432978")
    }

    @Test
    fun testPart1test2() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = processGame(numbers, 10)
        assert(result == listOf(8,3,7,4,1,9,2,6,5))
    }

    @Test
    fun testPart1test2Result() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = getResult(processGame(numbers, 10))
        assert(result == "92658374")
    }

    @Test
    fun testPart1test2Result100() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = getResult(processGame(numbers, 100))
        assert(result == "67384529")
    }

//    @Test
//    fun testPart1test2() {
//        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
//        val lotsOfNumbers = numbers + 6..1000000L
//        val result = processGame(numbers, 10)
//        assert(result == listOf(8,3,7,4,1,9,2,6,5))
//    }

    private fun getResult(numbers: List<Int>): String {
        val index = numbers.indexOf(1)
        val before = numbers.take(index)
        val after = numbers.takeLast(numbers.size - index - 1)
        return (after + before).joinToString("")
    }

    private tailrec fun processGame(numbers: List<Int>, roundsToGo: Int): List<Int> {
        if(roundsToGo == 0) {
            return numbers
        }
        else {
            val currentCup = numbers.first()
            val remainingCups = numbers.drop(1)
            val threeCups = remainingCups.take(3)
            val allCupsWithoutThree = listOf(currentCup) + remainingCups.drop(3)
            val index = findInsertionPoint(allCupsWithoutThree, currentCup)

            if(currentCup == allCupsWithoutThree[index]) {
                println("current cup is insertion point")
            }

            val before = allCupsWithoutThree.take(index+1)
            val after = allCupsWithoutThree.takeLast(allCupsWithoutThree.size - index - 1)
            val rearrangedRemainingCups = before +
                    threeCups +
                    after

            return processGame(rearrangedRemainingCups.drop(1) + rearrangedRemainingCups.first(), roundsToGo-1)
        }
    }

    private tailrec fun findInsertionPoint(allCupsWithoutThree: List<Int>, currentCup: Int): Int {
        val nextCup = when(currentCup) {
            1 -> 9
            else -> currentCup - 1
        }

        val index = allCupsWithoutThree.indexOf(nextCup)

        return if(index == -1)
            findInsertionPoint(allCupsWithoutThree, nextCup)
        else
            index
    }
}