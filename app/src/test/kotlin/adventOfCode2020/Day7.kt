package adventOfCode2020

import kotlin.test.Test

class Day7 {
    @Test
    fun testDay6() {
        val data = getFileAsListOfLines("/day7")
        val mapData = makeMap(data)
        val numberOfBags = mapData.keys.map { bagContainsGold(it, mapData) }

        assert(numberOfBags.map { if(it) 1 else 0 }.sum() == 370)
    }

    @Test
    fun testDay6Part2() {
        val data = getFileAsListOfLines("/day7")
        val mapData = makeMap(data)

        assert(bagCount("shiny gold", mapData) == 29547)
    }

    private fun bagCount(bag: String, mapData: Map<String, List<Pair<String, Int>>>): Int {
        return mapData[bag]?.map { i -> (bagCount(i.first, mapData)+1) * i.second }?.sum() ?: 0
    }

    private fun bagContainsGold(bag: String, mapData: Map<String, List<Pair<String, Int>>>): Boolean {
        return mapData[bag]?.map { i -> i.first == "shiny gold" || bagContainsGold(i.first, mapData) }?.contains(true) ?: false
    }

    private fun makeMap(l: List<String>) : Map<String, List<Pair<String, Int>>> {
        return l.map{ item -> makeEntry(item) }.toMap()
    }

    private fun makeEntry(item: String) : Pair<String, List<Pair<String, Int>>> {
        val parts = item.replace(".", "").split(" contain ")
        val entries = parts[1].split("\\ contain\\ ".toRegex())
        val bagsStrings = entries[0].split(",\\ ".toRegex())
        val bagsWithNumber = bagsStrings.map { i -> i.replace("no other", "0").replace("bags", "").replace("bag", "") }
        val bagsWithEmpty = bagsWithNumber.map{ i-> Pair(i.substring(2), i[0].toString().toInt())}
        val bags = if (bagsWithNumber.size == 1 && bagsWithEmpty[0] == Pair("", 0)) listOf<Pair<String, Int>>() else bagsWithEmpty
        val bagsProcessed = bags.map{ i -> Pair(i.first.trim(), i.second)}

        return parts[0].replace(" bags", "") to bagsProcessed
    }
}
