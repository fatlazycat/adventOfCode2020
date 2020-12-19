package adventOfCode2020

import org.junit.Test
import java.lang.UnsupportedOperationException

class Day19 {

    @Test
    fun testPart1() {
        val rules = processRules(getFileAsListOfLines("/day19rules"))
        val messages: List<String> = getFileAsListOfLines("/day19messages")
        val expandedRules = expandRules(0, rules)

        assert(messages.map { expandedRules.contains(it) }.filter { it }.count() == 192)
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

        assert(messages.map { expandedRules.contains(it) }.filter { it }.count() == 2)
    }

//    @Test
//    fun testPart2() {
//        val initialRules = processRules(getFileAsListOfLines("/day19rules"))
//
//        // alter rules
//        val rules = initialRules.toMutableMap()
////        rules[8] = Rule(0, c = '8')
////        rules[11] = Rule(0, c = '?')
////        val ex = expandRules(0, rules)
//        rules[8] = Rule(2, left = listOf(42), right = listOf(42, 8))
//        rules[11] = Rule(2, left = listOf(42, 31), right = listOf(42, 11, 31))
//
//        val messages: List<String> = getFileAsListOfLines("/day19messages")
//        val rules42 = expandRules(42, rules)
//        val rules31 = expandRules(31, rules)
//
//        val notMatching = messages.map { m ->
//            if (!messageMatches(rules42, rules31, m)) {
//                m
//            }
//            else {
//                null
//            }
//        }.filterNotNull()
//
//        println(messages.map { messageMatches(rules42, rules31, it) }.filter { it }.count())
////        assert(messages.map { messageMatches(rules42, rules31, it) }.filter { it }.count() == 12)
//    }

    @Test
    fun testPart2() {
        val initialRules = processRules(getFileAsListOfLines("/day19rules"))

        // alter rules
        val rules = initialRules.toMutableMap()
//        rules[8] = Rule(0, c = '8')
//        rules[11] = Rule(0, c = '?')
//        val ex = expandRules(0, rules)
        rules[8] = Rule(2, left = listOf(42), right = listOf(42, 8))
        rules[11] = Rule(2, left = listOf(42, 31), right = listOf(42, 11, 31))

        val messages: List<String> = getFileAsListOfLines("/day19messages")
        val rules42 = expandRules(42, rules)
        val rules31 = expandRules(31, rules)

//        val matches = rules42.toSet().intersect(rules31.toSet())

        println(messages.map { messageMatches2(rules42, rules31, it) }.filter { it }.count())
//        assert(messages.map { messageMatches(rules42, rules31, it) }.filter { it }.count() == 12)
    }

    @Test
    fun testPart2test() {
        val initialRules = processRules(getFileAsListOfLines("/day19rulesTest2"))

        // alter rules
        val rules = initialRules.toMutableMap()
        rules[8] = Rule(2, left = listOf(42), right = listOf(42, 8))
        rules[11] = Rule(2, left = listOf(42, 31), right = listOf(42, 11, 31))

        val messages: List<String> = getFileAsListOfLines("/day19messagesTest2")
        val rules42 = expandRules(42, rules)
        val rules31 = expandRules(31, rules)

//        val matches = rules42.toSet().intersect(rules31.toSet())

        assert(messageMatches2(rules42, rules31, "bbabbbbaabaabba"))
        assert(!messageMatches2(rules42, rules31, "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa"))

        assert(messages.map { messageMatches2(rules42, rules31, it) }.filter { it }.count() == 12)
    }

//    private fun messageMatches(rule42: List<String>, rule31: List<String>, message: String): Boolean {
//        val matchesOf42 = portionThatMatches42(rule42, message, "", 0)
//
//        return if(matchesOf42.second >= 2) {
//            val remainingMessage = message.substring(matchesOf42.first.length)
//            val matchesOf31 = portionThatMatches31(rule31, remainingMessage, "", 0)
//
//            if(matchesOf31.second >= 1) {
//                matchesOf31.first.length == remainingMessage.length
//            } else {
//                false
//            }
//        } else {
//            false
//        }
//    }

    private fun messageMatches2(rule42: List<String>, rule31: List<String>, message: String): Boolean {
        val matchesOf31 = endPortionThatMatches31(rule31, message, "", 0)
        val matchesOf42 = portionThatMatches42(rule42, message, "", 0)

        return matchesOf31.second > 0 && matchesOf42.second > 0 && (matchesOf42.second - matchesOf31.second) >= 1
    }

    private fun portionThatMatches42(rule42: List<String>, message: String, current: String, matches: Int): Pair<String, Int> {
        val match42 = rule42.find { message.startsWith(it) }

        return when {
            match42 == null -> {
                Pair(current, matches)
            }
            match42.length == message.length -> {
                Pair(current + match42, matches+1)
            }
            else -> {
                portionThatMatches42(rule42, message.substring(match42.length), current + match42, matches+1)
            }
        }
    }

//    private fun portionThatMatches31(rule31: List<String>, message: String, current: String, matches: Int): Pair<String, Int> {
//        val match31 = rule31.find { message.startsWith(it) }
//
//        return when {
//            match31 == null -> {
//                Pair(current, matches)
//            }
//            match31.length == message.length -> {
//                Pair(current + match31, matches+1)
//            }
//            else -> {
//                portionThatMatches31(rule31, message.substring(match31.length), current + match31, matches+1)
//            }
//        }
//    }

    private fun endPortionThatMatches31(rule31: List<String>, message: String, current: String, matches: Int): Pair<String, Int> {
        val match31 = rule31.find { message.endsWith(it) }

        return when {
            match31 == null -> {
                Pair(current, matches)
            }
            match31.length == message.length -> {
                Pair(match31 + current, matches+1)
            }
            else -> {
                endPortionThatMatches31(rule31, message.substring(0, message.length-match31.length), match31 + current, matches+1)
            }
        }
    }

    private fun expandRules(ruleIndex: Int, rules: Map<Int, Rule>): List<String> {
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
        return if (l.size == 1)
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

