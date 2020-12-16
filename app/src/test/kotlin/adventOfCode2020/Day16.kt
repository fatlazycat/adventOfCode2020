package adventOfCode2020

import org.junit.Test

class Day16 {

    private val regex = """ (\d*)-(\d*) or (\d*)-(\d*)""".toRegex()


    @Test
    fun testPart1() {
//        val data = processInput("/day16testrules", "/day16testticket", "/day16testnearby")
        val data = processInput("/day16rules", "/day16ticket", "/day16nearby")
        val errorRate = findInvalidEntries(data.first, data.third)
        println(errorRate)
    }

    private fun processInput(s1: String, s2: String, s3: String): Triple<List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>, List<Int>, List<List<Int>>> {
        val rulesLines: List<String> = getFileAsListOfLines(s1)
        val ticketLines: List<String> = getFileAsListOfLines(s2)
        val nearbyLines: List<String> = getFileAsListOfLines(s3)

        val rules = rulesLines.map { l -> getRule(l) }
        val ticket = ticketLines[1].split(",").map{ it.trim().toInt() }
        val nearby = nearbyLines.drop(1).map{ it.split(",").map{ it.trim().toInt() }}

        return Triple(rules, ticket, nearby)
    }

    private fun findInvalidEntries(
        rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
        nearby: List<List<Int>>
    ): Int {
        val allNumbers = nearby.flatten()
        val invalidNumbers = allNumbers.filter { !isContained(it, rules) }

        println(allNumbers)
        return invalidNumbers.sum()
    }

    private tailrec fun isContained(num: Int, rules: List<Pair<String, Pair<Pair<Int, Int>, Pair<Int, Int>>>>): Boolean {
        if (rules.isEmpty())
            return false
        else {
            val e = rules.first()
            val bounds = e.second
            val b1 = bounds.first
            val b2 = bounds.second

            return if((num >= b1.first && num <= b1.second) || (num >= b2.first && num <= b2.second))
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

