package adventOfCode2020

import org.junit.Test

class Day16 {

    private val regex = """ (\d*)-(\d*) or (\d*)-(\d*)""".toRegex()

    @Test
    fun testPart1() {
        val data = processInput("/day16rules", "/day16ticket", "/day16nearby")
        val errorRate = findInvalidEntries(data.first, data.third)

        assert(errorRate == 28873)
    }

    @Test
    fun testPart2() {
        val data = processInput("/day16rules", "/day16ticket", "/day16nearby")
        val validNearby = data.third.filter { isEntryValid(it, data.first) }
        val possibleEntries = mapNumbersToPossibleSolution(data.first, validNearby)
        val fieldNumbers = processPossibleEntries(possibleEntries, data.first)
        val answer = processKnownData(fieldNumbers)
        val convertedAnswer = answer.entries.map{ p -> p.key to p.value.first() }
        val departures = convertedAnswer.filter { p -> p.second.startsWith("departure") }
        val total = departures.fold(1L){ acc, i -> acc * data.second[i.first]}

        assert(total == 2587271823407)
    }

    private fun processKnownData(data: MutableMap<Int, MutableList<String>>) : Map<Int, List<String>> {
        val orderedValues = data.values.sortedBy { it.count() }

        orderedValues.mapIndexed{ index, candidates ->
            if (candidates.count() == 1) {
                // remove this entry from all the rest
                orderedValues.mapIndexed{ i, j ->
                    if(i != index) {
                        j.remove(candidates.first())
                    }
                }
            }
        }

        return data
    }

    private fun processPossibleEntries(
        entries: List<List<Pair<Int, List<String>>>>,
        rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>
    ): MutableMap<Int, MutableList<String>> {
        val allRules = rules.map { it.first }.toSet()
        val result: MutableMap<Int, MutableList<String>> =
            allRules.mapIndexed{ index, _ -> index to allRules.toMutableList() }.toMap().toMutableMap()

        entries.map { ticket ->
            ticket.mapIndexed { index, field ->
                if(field.second.size < allRules.size) {
                    val intersection = allRules.intersect(field.second)
                    val missingEntries = allRules - intersection

                    if (missingEntries.size == 1) {
                        val currentEntries = result[index]!!
                        currentEntries.remove(missingEntries.first())
                    }
                }

            }
        }

        return result
    }

    private fun mapNumbersToPossibleSolution(
        rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
        nearby: List<List<Int>>):
            List<List<Pair<Int, List<String>>>> {

        return nearby.map{ it.map { n -> Pair(n, validOptions(n, rules)) }}
    }

    private fun validOptions(n: Int, rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>): List<String> {
        return rules.mapNotNull { rule -> if (isContained(n, listOf(rule))) rule.first else null }
    }

    private fun processInput(s1: String, s2: String, s3: String): Triple<List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>, List<Long>, List<List<Int>>> {
        val rulesLines: List<String> = getFileAsListOfLines(s1)
        val ticketLines: List<String> = getFileAsListOfLines(s2)
        val nearbyLines: List<String> = getFileAsListOfLines(s3)

        val rules = rulesLines.map { l -> getRule(l) }
        val ticket = ticketLines[1].split(",").map{ it.trim().toLong() }
        val nearby = nearbyLines.drop(1).map{ i -> i.split(",").map{ it.trim().toInt() }}

        return Triple(rules, ticket, nearby)
    }

    private fun findInvalidEntries(
        rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
        nearby: List<List<Int>>
    ): Int {
        val allNumbers = nearby.flatten()
        return allNumbers.filter { !isContained(it, rules) }.sum()
    }

    private fun isEntryValid(entry: List<Int>, rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>): Boolean {
        return entry.none { !isContained(it, rules) }
    }

    private tailrec fun isContained(num: Int, rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>): Boolean {
        return if (rules.isEmpty())
            false
        else {
            val e = rules.first()
            val bounds = e.second
            val b1 = bounds.first
            val b2 = bounds.second

            if((num >= b1.first && num <= b1.second) || (num >= b2.first && num <= b2.second))
                true
            else
                isContained(num, rules.drop(1))
        }
    }

    private fun getRule(s: String): Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val parts = s.split(":")

        regex.find(parts[1])!!.groupValues.let { (_, n1, n2, n3, n4) ->
            return Pair(parts[0],
                Pair(Pair(n1.toInt(), n2.toInt()), Pair(n3.toInt(), n4.toInt())))
        }
    }
}

