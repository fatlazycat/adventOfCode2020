package adventOfCode2020

import kotlin.streams.toList

fun getFileAsListOfLines(fileName: String) =
    object {}::class.java.getResourceAsStream(fileName).bufferedReader().lines().toList()