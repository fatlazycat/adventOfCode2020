package adventOfCode2020

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.collections.immutable.*
import org.junit.Test

class Day15 {
    private val data = listOf(18, 11, 9, 0, 5, 1)
    private val testData1 = listOf(0, 3, 6)
    private val testData2 = listOf(1, 3, 2)
    private val testData3 = listOf(2, 1, 3)
    private val testData4 = listOf(1, 2, 3)
    private val testData5 = listOf(2, 3, 1)
    private val testData6 = listOf(3, 2, 1)
    private val testData7 = listOf(3, 1, 2)

    @Test
    fun testPart1() {
        assert(numberSmall(testData1, 2020) == 436)
        assert(numberSmall(testData2, 2020) == 1)
        assert(numberSmall(testData3, 2020) == 10)
        assert(numberSmall(testData4, 2020) == 27)
        assert(numberSmall(testData5, 2020) == 78)
        assert(numberSmall(testData6, 2020) == 438)
        assert(numberSmall(testData7, 2020) == 1836)
        assert(numberSmall(data, 2020) == 959)
    }

    @Test
    fun testPart2() {
        assert(numberLarge(testData1, 30000000) == 175594)
        assert(numberLarge(data, 30000000) == 116590)
    }

    @Test
    fun testTailrecWithPersistentMap() {
        assert(numberTailRec(testData1, 2020) == 436)
        assert(numberTailRec(testData2, 2020) == 1)
        assert(numberTailRec(testData3, 2020) == 10)
        assert(numberTailRec(testData4, 2020) == 27)
        assert(numberTailRec(testData5, 2020) == 78)
        assert(numberTailRec(testData6, 2020) == 438)
        assert(numberTailRec(testData7, 2020) == 1836)
        assert(numberTailRec(data, 2020) == 959)
        assert(numberTailRec(data, 30000000) == 116590)
    }

    private fun numberSmall(startingNumbers: List<Int>, stopNum: Int): Int {
        val result = (startingNumbers.size until stopNum).fold(startingNumbers) { acc, _ ->
            val toFind = acc.last()
            val foundIndex = acc.lastIndexOf(toFind)
            val prevIndex = acc.dropLast(1).lastIndexOf(toFind)

            if (foundIndex == -1)
                acc + toFind
            else if (foundIndex != -1 && prevIndex == -1)
                acc + 0
            else
                acc + (foundIndex - prevIndex)
        }

        return result.last()
    }

    private fun numberLarge(startingNumbers: List<Int>, stopNum: Int): Int {
        val locations = startingNumbers.mapIndexed { index, n -> Pair(n, Pair(index, -1)) }.toMap(mutableMapOf())
        var toFind = startingNumbers.last()

        for (i in (startingNumbers.size until stopNum)) {
            val indices = locations[toFind]

            when {
                indices == null -> {
                    locations[toFind] = Pair(i, -1)
                }
                indices.second == -1 -> {
                    toFind = 0
                    val indicesOfZero = locations[0]!!
                    val maxIndexOfZero = maxOf(indicesOfZero.first, indicesOfZero.second)
                    locations[0] = Pair(maxIndexOfZero, i)
                }
                else -> {
                    val foundIndex = indices.second
                    val prevIndex = indices.first
                    toFind = foundIndex - prevIndex
                    val newIndices = locations[toFind]

                    when {
                        newIndices == null -> locations[toFind] = Pair(i, -1)
                        newIndices.second == -1 -> locations[toFind] = Pair(newIndices.first, i)
                        else -> locations[toFind] = Pair(newIndices.second, i)
                    }
                }
            }
        }

        return toFind
    }

    private fun numberTailRec(l: List<Int>, nth: Int) =
        numberFun(l.last(), l.size, nth, l.mapIndexed { index, n -> n to Pair(index, -1) }.toMap().toPersistentMap())

    private tailrec fun numberFun(
        toFind: Int,
        current: Int,
        stopNum: Int,
        locations: PersistentMap<Int, Pair<Int, Int>>
    ): Int {
        if (current == stopNum)
            return toFind
        else {
            val indices = locations[toFind]
            val newNum: Int =
                when {
                    indices == null -> toFind
                    indices.second == -1 -> 0
                    else -> indices.second - indices.first
                }

            val newIndices = locations[newNum]

            return when {
                newIndices == null -> numberFun(
                    newNum,
                    current + 1,
                    stopNum,
                    locations + (newNum to Pair(current, -1))
                )
                newIndices.second == -1 -> numberFun(
                    newNum,
                    current + 1,
                    stopNum,
                    locations + (newNum to Pair(newIndices.first, current))
                )
                else -> numberFun(
                    newNum,
                    current + 1,
                    stopNum,
                    locations + (newNum to Pair(newIndices.second, current))
                )
            }
        }
    }
}