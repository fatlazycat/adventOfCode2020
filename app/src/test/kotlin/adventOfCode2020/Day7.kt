package adventOfCode2020

import kotlin.test.Test

fun CharSequence.splitIgnoreEmpty(vararg delimiters: String): List<String> {
    return this.split(*delimiters).filter {
        it.isNotEmpty()
    }
}

class Day7 {
    private val shinyGold = "shiny gold"

    @Test
    fun testDay6() {
        val data = getFileAsListOfLines("/day7")
        val mapData = makeMap(data)
        val numberOfBags = mapData.keys.map { bagContainsGold(it, mapData) }

        assert(numberOfBags.map { if (it) 1 else 0 }.sum() == 370)
    }

    @Test
    fun testDay6Part2() {
        val data = getFileAsListOfLines("/day7")
        val mapData = makeMap(data)

        assert(bagCount(shinyGold, mapData) == 29547)
    }

    private fun bagCount(bag: String, mapData: Map<String, List<Pair<String, Int>>>): Int {
        return mapData[bag]?.map { i -> (bagCount(i.first, mapData) + 1) * i.second }?.sum() ?: 0
    }

    private fun bagContainsGold(bag: String, mapData: Map<String, List<Pair<String, Int>>>): Boolean {
        return mapData[bag]?.map { i -> i.first == shinyGold || bagContainsGold(i.first, mapData) }?.contains(true)
            ?: false
    }

    private fun makeMap(l: List<String>): Map<String, List<Pair<String, Int>>> {
        return l.map { item -> makeEntry(item) }.toMap()
    }

    private fun makeEntry(item: String): Pair<String, List<Pair<String, Int>>> {
        val regex = """([a-z0-9 ]*)(?: bags contain )([a-z0-9\s].*,)*?([a-z0-9\s]*\.|no\sother\sbags\.)""".toRegex()

        regex.find(item)!!.groupValues.let { (_, bag, body, end) ->
            return Pair(bag, makeListEntry(body) + makeListEntry(end))
        }
    }

    private fun makeListEntry(s: String) : List<Pair<String, Int>> {
        return if (s == "no other bags.")
            listOf()
        else {
            val parts = s.splitIgnoreEmpty(",", ".").map { it.trim() }
            parts.map { i -> Pair(i.substring(2, i.length-4).trim(), i[0].toString().toInt()) }
        }
    }
}
