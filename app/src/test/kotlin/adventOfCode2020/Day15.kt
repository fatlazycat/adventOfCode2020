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

//    Given the starting numbers 1,3,6, the 2020th number spoken is 436.
//    Given the starting numbers 1,3,2, the 2020th number spoken is 1.
//    Given the starting numbers 2,1,3, the 2020th number spoken is 10.
//    Given the starting numbers 1,2,3, the 2020th number spoken is 27.
//    Given the starting numbers 2,3,1, the 2020th number spoken is 78.
//    Given the starting numbers 3,2,1, the 2020th number spoken is 438.
//    Given the starting numbers 3,1,2, the 2020th number spoken is 1836.

    @Test
    fun testPart1() {
        println(number2020(testData1))
    }

    private fun number2020(startingNumbers: List<Int>): Int {
        val numbers = (3 until 2020).map { i->
            Pair(i, startingNumbers[i.rem(3)])
        }

        val result = numbers.fold(startingNumbers){ acc, p ->
            val toFind = acc.last()
            val foundIndex = acc.lastIndexOf(toFind)
            val prevIndex = acc.dropLast(1).lastIndexOf(toFind)

            if(foundIndex == -1)
                acc + toFind
            else if(foundIndex != -1 && prevIndex == -1)
                acc + 0
            else
                acc + (p.first-foundIndex)
        }

        return result.last()
    }
}