package adventOfCode2020

import org.junit.Test
import java.lang.UnsupportedOperationException

class Day19 {

    @Test
    fun testPart1() {
        val rules = processRules(getFileAsListOfLines("/day19rules"))
        val messages: List<String> = getFileAsListOfLines("/day19messages")
        val expandedRules = expandRules(0, rules)

        println(messages.map { expandedRules.contains(it) }.filter { it }.count())
    }

    @Test
    fun testPart1test() {
        val rules = processRules(getFileAsListOfLines("/day19rulesTest"))
        val messages: List<String> = getFileAsListOfLines("/day19messagesTest")
        val expandedRules = expandRules(0, rules)

        assert(expandedRules.contains(messages[0]))
        assert(!expandedRules.contains(messages[1]))
        assert(expandedRules.contains(messages[2]))
        assert(!expandedRules.contains(messages[3]))
        assert(!expandedRules.contains(messages[4]))
    }

    private fun expandRules(ruleIndex: Int, rules: Map<Int, Rule>) : List<String> {
        val rule = rules[ruleIndex]!!

        return when (rule.type) {
            0 -> {
                listOf(rule.c.toString())
            }
            1 -> {
                val parts = rule.left.map { r -> expandRules(r, rules) }
                val combined = allPossibleString(parts)
                combined
            }
            2 -> {
                val partsLeft = rule.left.map { r -> expandRules(r, rules) }
                val partsRight = rule.right.map { r -> expandRules(r, rules) }
                val combinedLeft = allPossibleString(partsLeft)
                val combinedRight = allPossibleString(partsRight)
                combinedLeft + combinedRight
            }
            else -> throw UnsupportedOperationException()
        }
    }

    @Test
    fun testAllPossibleString() {
        val options = allPossibleString(listOf(listOf("a"), listOf("b", "c"), listOf("d", "e")))
        println(options)
    }

    private fun allPossibleString(l: List<List<String>>): List<String> {
        return if(l.size == 1)
            l[0]
        else {
            val first = l.first()
            val result = first.map { firstPart ->
               allPossibleString(l.drop(1)).map { nextPart ->
                   val combined = firstPart + nextPart
                   combined
               }
            }.flatten()

            result
        }
    }

    private fun processRules(data: List<String>): Map<Int, Rule> {
        return data.map { l ->
            val parts = l.split(":")
            val ruleNum = parts[0].toInt()

            if (parts[1].toCharArray().contains('"')) {
                val letter = parts[1].trim()[1]
                ruleNum to Rule(0, c = letter)
            } else if (parts[1].toCharArray().contains('|')) {
                val numParts = parts[1].split("|")
                val left = processNumbers(numParts[0].trim())
                val right = processNumbers(numParts[1].trim())
                ruleNum to Rule(2, left = left, right = right)
            } else {
                val left = processNumbers(parts[1].trim())
                ruleNum to Rule(1, left = left)
            }
        }.toMap()
    }

    private fun processNumbers(s: String): List<Int> {
        val parts = s.split(" ")
        return parts.map { it.toInt() }
    }

    // 0 for letter
    // 1 for one set of num
    // 2 for two sets of num
    private data class Rule(
        val type: Int,
        val c: Char? = null,
        val left: List<Int> = listOf(),
        val right: List<Int> = listOf()
    )
}

