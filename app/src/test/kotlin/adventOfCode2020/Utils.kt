package adventOfCode2020

import kotlin.streams.toList

fun getFileAsListOfLines(fileName: String) =
    object {}::class.java.getResourceAsStream(fileName).bufferedReader().lines().toList()

fun splitPoints(testData: List<String>) =
    testData.mapIndexed { index, item -> if (item.isNullOrEmpty()) index else -1 }.filter { it != -1 }

fun dataSet(
    splitPoints: List<Int>,
    testData: List<String>
) = (listOf(-1) + splitPoints + testData.size)
    .windowed(2, 1)
    .map { (start, end) -> testData.subList(start + 1, end) }
    .map { i -> i.map { j -> j.split(" ") }.flatten() }