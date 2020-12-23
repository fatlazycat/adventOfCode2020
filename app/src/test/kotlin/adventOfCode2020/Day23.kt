package adventOfCode2020

import org.junit.Test

class Day23 {
    private val puzzleInput = "962713854"
    private val puzzleInputTest = "389125467"

    @Test
    fun testPart1() {
        val numbers = puzzleInput.toCharArray().map { it.toString().toInt() }.toList()
        val result = getResult(processGame(numbers, 100))
        assert(result == "65432978")
    }

    @Test
    fun testPart1test() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = processGame(numbers, 10)
        assert(result == listOf(8, 3, 7, 4, 1, 9, 2, 6, 5))
    }

    @Test
    fun testPart1testResult() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = getResult(processGame(numbers, 10))
        assert(result == "92658374")
    }

    @Test
    fun testPart1testResult100() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = getResult(processGame(numbers, 100))
        assert(result == "67384529")
    }

    @Test
    fun testPart2() {
        val numbers = puzzleInput.toCharArray().map { it.toString().toInt() - 1}.toList()
        val result = processGameArrayOfNextLocations(numbers, 10000000, 1000000)
        val n1 = result[0]
        val n2 = result[n1]
        val answer = (n1+1).toLong() * (n2+1).toLong()

        assert(answer == 287230227046)
    }

    @Test
    fun testPart2test() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() - 1}.toList()
        val result = processGameArrayOfNextLocations(numbers, 10000000, 1000000)
        val n1 = result[0]
        val n2 = result[n1]
        val answer = (n1+1).toLong() * (n2+1).toLong()

        assert( answer == 149245887792L)
    }

    private fun processGameArrayOfNextLocations(numbers: List<Int>, rounds: Int, totalNumbers: Int ) : IntArray {
        // create array where contents of index points to next location
        val arr = IntArray(totalNumbers) { it + 1 }
        arr[arr.lastIndex] = numbers.first()

        // put the data set into appropriate contents
        for ((i, n) in numbers.withIndex()) {
            arr[n] = numbers.getOrElse(i+1) { 9 }
        }

        // make last index point to
        var x = numbers.first()
        arr[arr.lastIndex] = x

        repeat(rounds) {
            x = playRound(arr, x)
        }
        return arr
    }

    private fun playRound(arr: IntArray, current: Int): Int {
        val next1 = arr[current]
        val next2 = arr[next1]
        val next3 = arr[next2]
        val next4 = arr[next3]
        var insertionNumber = current

        // get insertion point, skip matching of then three number
        do {
            insertionNumber = if (insertionNumber > 0) insertionNumber - 1 else arr.lastIndex
        } while (insertionNumber == next1 || insertionNumber == next2 || insertionNumber == next3)

        val whereInsertionUseToPoint = arr[insertionNumber]

        // repair the hole
        arr[current] = next4

        // insert the three numbers in correct location
        arr[insertionNumber] = next1
        arr[next3] = whereInsertionUseToPoint

        // return the next number to process
        return next4
    }

    private fun getResult(numbers: List<Int>): String {
        val index = numbers.indexOf(1)
        val before = numbers.take(index)
        val after = numbers.takeLast(numbers.size - index - 1)
        return (after + before).joinToString("")
    }

    private tailrec fun processGame(numbers: List<Int>, roundsToGo: Int): List<Int> {
        if (roundsToGo == 0) {
            return numbers
        } else {
            val currentCup = numbers.first()
            val remainingCups = numbers.drop(1)
            val threeCups = remainingCups.take(3)
            val allCupsWithoutThree = listOf(currentCup) + remainingCups.drop(3)
            val index = findInsertionPoint(allCupsWithoutThree, currentCup)
            val before = allCupsWithoutThree.take(index + 1)
            val after = allCupsWithoutThree.takeLast(allCupsWithoutThree.size - index - 1)
            val rearrangedRemainingCups = before +
                    threeCups +
                    after

            return processGame(rearrangedRemainingCups.drop(1) + rearrangedRemainingCups.first(), roundsToGo - 1)
        }
    }

    private tailrec fun findInsertionPoint(allCupsWithoutThree: List<Int>, currentCup: Int): Int {
        val nextCup = when (currentCup) {
            1 -> 9
            else -> currentCup - 1
        }

        val index = allCupsWithoutThree.indexOf(nextCup)

        return if (index == -1)
            findInsertionPoint(allCupsWithoutThree, nextCup)
        else
            index
    }
}

