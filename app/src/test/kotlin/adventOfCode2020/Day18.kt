package adventOfCode2020

import org.junit.Test

class Day18 {
    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day18")
        val results = lines.map{ evalEqualPrecedence(it) }
        assert(results.sum() == 11004703763391)
    }

    @Test
    fun testPart1test() {
        val lines: List<String> = getFileAsListOfLines("/day18test")
        val results = lines.map{ evalEqualPrecedence(it) }
        assert(results.sum() == 26335L)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day18")
        val results = lines.map{ evalPlusFirst(it) }
        assert(results.sum() == 290726428573651)
    }

    @Test
    fun testPart2test() {
        val lines: List<String> = getFileAsListOfLines("/day18test2")
        val results = lines.map{ evalPlusFirst(it) }
        assert(results.sum() == 693942L)
    }
}

const val ZERO = '0'.toInt()
const val NINE = '9'.toInt()
const val SPACE = ' '.toInt()
const val LEFT = '('.toInt()
const val RIGHT = ')'.toInt()
const val MULTIPLY = '*'.toInt()
const val ADD = '+'.toInt()

fun evalEqualPrecedence(str: String): Long {
    return object : Any() {
        var pos = -1
        var ch = 0
        fun nextChar() {
            ch = if (++pos < str.length) str[pos].toInt() else -1
        }

        fun eat(charToEat: Int): Boolean {
            while (ch == SPACE) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun eatSpace() {
            while (ch == SPACE) nextChar()
        }

        fun parse(): Long {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
            return x
        }

        fun parseExpression(): Long {
            var x = parseFactor()
            while (true) {
                when {
                    eat(ADD) -> x += parseFactor()
                    eat(MULTIPLY) -> x *= parseFactor()
                    else -> return x
                }
            }
        }

        fun parseFactor(): Long {
            eatSpace()
            val x: Long
            val startPos = pos
            when {
                eat(LEFT) -> { // parentheses
                    x = parseExpression()
                    eat(RIGHT)
                }
                ch in ZERO..NINE -> { // numbers
                    while (ch in ZERO..NINE) nextChar()
                    x = str.substring(startPos, pos).toLong()
                }
                else -> {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
            }

            return x
        }
    }.parse()
}

fun evalPlusFirst(str: String): Long {
    return object : Any() {
        var pos = -1
        var ch = 0
        fun nextChar() {
            ch = if (++pos < str.length) str[pos].toInt() else -1
        }

        fun eat(charToEat: Int): Boolean {
            while (ch == SPACE) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun eatSpace() {
            while (ch == SPACE) nextChar()
        }

        fun parse(): Long {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
            return x
        }

        fun parseExpression(): Long {
            var x = parseFactor()
            while (true) {
                when {
                    eat(ADD) -> x += parseFactor()
                    eat(MULTIPLY) -> x *= parseExpression()
                    else -> return x
                }
            }
        }

        fun parseFactor(): Long {
            eatSpace()

            val x: Long
            val startPos = pos
            when {
                eat(LEFT) -> { // parentheses
                    x = parseExpression()
                    eat(RIGHT)
                }
                ch in ZERO..NINE -> { // numbers
                    while (ch in ZERO..NINE) nextChar()
                    x = str.substring(startPos, pos).toLong()
                }
                else -> {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
            }
            return x
        }
    }.parse()
}