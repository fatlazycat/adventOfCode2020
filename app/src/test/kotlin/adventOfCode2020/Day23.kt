package adventOfCode2020

import org.junit.Test
import java.util.*

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
    fun testPart1testMutable() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val puzzle = LinkedList<Int>()
        puzzle.addAll(numbers)

        val result = processGameMutable(puzzle, 10)
        assert(result == listOf(8, 3, 7, 4, 1, 9, 2, 6, 5))
    }

    @Test
    fun testPart1Mutable() {
        val numbers = puzzleInput.toCharArray().map { it.toString().toInt() }.toList()
        val puzzle = LinkedList<Int>()
        puzzle.addAll(numbers)

        val result = getResult(processGameMutable(puzzle, 100))
        assert(result == "65432978")
    }

    @Test
    fun testPart2testArrayOfIndexes() {
        val numbers = puzzleInputTest.toCharArray().map { it.toString().toInt() }.toList()
        val result = processGameArray(numbers, 10000000)
        val n1 = result[0]
        val n2 = result[n1]
        val answer = (n1+1).toLong() * (n2+1).toLong()

        assert( answer == 149245887792L)
    }

    @Test
    fun testPart2ArrayOfIndexes() {
        val numbers = puzzleInput.toCharArray().map { it.toString().toInt() }.toList()
        val result = processGameArray(numbers, 10000000)
        val n1 = result[0]
        val n2 = result[n1]
        val answer = (n1+1).toLong() * (n2+1).toLong()

        assert(answer == 287230227046)
    }

    private fun processGameArray(numbers: List<Int>, rounds: Int) : IntArray {
        val arr = IntArray(1000000) { it + 1 }
        for ((i, n) in numbers.withIndex()) {
            arr[n - 1] = numbers.getOrElse(i + 1) { 10 } - 1
        }
        var x = numbers.first() - 1
        arr[arr.lastIndex] = x
        repeat(10000000) { x = step(arr, x) }
        return arr
    }

    private fun step(arr: IntArray, x: Int): Int {
        val a = arr[x]
        val b = arr[a]
        val c = arr[b]
        val y = arr[c]
        var t = x
        do {
            t = if (t > 0) t - 1 else arr.lastIndex
        } while (t == a || t == b || t == c)
        val u = arr[t]
        arr[x] = y
        arr[t] = a
        arr[c] = u
        return y
    }

    private fun getResult(numbers: List<Int>): String {
        val index = numbers.indexOf(1)
        val before = numbers.take(index)
        val after = numbers.takeLast(numbers.size - index - 1)
        return (after + before).joinToString("")
    }

    private fun processGameMutable(numbers: LinkedList<Int>, roundsToGo: Int): LinkedList<Int> {

        for (i in 0 until roundsToGo) {
            if (i % 1000 == 0)
                println("$i")

            val currentCup = numbers[0]
            val n1 = numbers[1]
            val n2 = numbers[2]
            val n3 = numbers[3]
            val index = findInsertionPoint(numbers, currentCup, n1, n2, n3)

            numbers.addAll(index + 1, mutableListOf(n1, n2, n3))

            numbers.remove()
            numbers.remove()
            numbers.remove()
            numbers.remove()

            numbers.add(currentCup)
        }

        return numbers
    }

    private fun findInsertionPoint(numbers: LinkedList<Int>, current: Int, n1: Int, n2: Int, n3: Int): Int {
        val size = numbers.count()
        var n = current - 1

        if (n == 0)
            n = size

        while (n == n1 || n == n2 || n == n3) {
            n -= 1

            if (n == 0)
                n = size
        }

        return numbers.indexOf(n)
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

