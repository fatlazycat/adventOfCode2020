package adventOfCode2020

import org.junit.Test

class Day15 {
    private val data = listOf(18,11,9,0,5,1)
    private val testData1 = listOf(0,3,6)
    private val testData2 = listOf(1,3,2)
    private val testData3 = listOf(2,1,3)
    private val testData4 = listOf(1,2,3)
    private val testData5 = listOf(2,3,1)
    private val testData6 = listOf(3,2,1)
    private val testData7 = listOf(3,1,2)

    @Test
    fun testPart1() {
        assert(number2020(testData1) == 436)
        assert(number2020(testData2) == 1)
        assert(number2020(testData3) == 10)
        assert(number2020(testData4) == 27)
        assert(number2020(testData5) == 78)
        assert(number2020(testData6) == 438)
        assert(number2020(testData7) == 1836)
        assert(number2020(data) == 959)
    }

    @Test
    fun testPart2() {
        assert(numberLarge2020(testData1) == 175594)
        assert(numberLarge2020(data) == 116590)
    }

    private fun number2020(startingNumbers: List<Int>): Int {
        val result = (startingNumbers.size until 2020).fold(startingNumbers){ acc, _ ->
            val toFind = acc.last()
            val foundIndex = acc.lastIndexOf(toFind)
            val prevIndex = acc.dropLast(1).lastIndexOf(toFind)

            if(foundIndex == -1)
                acc + toFind
            else if(foundIndex != -1 && prevIndex == -1)
                acc + 0
            else
                acc + (foundIndex-prevIndex)
        }

        return result.last()
    }

    private fun numberLarge2020(startingNumbers: List<Int>): Int {
        val m = startingNumbers.mapIndexed{ index, n -> Pair(n, Pair(index, -1)) }.toMap(mutableMapOf())

        return process(startingNumbers.last(), m, startingNumbers.size)
    }

    private fun process(lastNum: Int, locations: MutableMap<Int, Pair<Int, Int>>, startingNumber: Int) : Int {
        var toFind = lastNum

        for(i in (startingNumber until 30000000)) {
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
}