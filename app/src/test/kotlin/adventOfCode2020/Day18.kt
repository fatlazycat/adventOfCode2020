package adventOfCode2020

import org.junit.Test
import java.lang.UnsupportedOperationException
import java.util.*

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

    @Test
    fun testPart1Shunt() {
        val lines: List<String> = getFileAsListOfLines("/day18")
        val results = lines.map { l -> evaluateRPN(shuntingYards(splitIntoToken(l), equal)) }
        assert(results.sum() == 11004703763391)
    }

    @Test
    fun testPart2Shunt() {
        val lines: List<String> = getFileAsListOfLines("/day18")
        val results = lines.map { l -> evaluateRPN(shuntingYards(splitIntoToken(l), plusGreaterThanMultiply)) }
        assert(results.sum() == 290726428573651)
    }

    private val allOperators = listOf("*", "/", "+", "-")
    private val normal = mapOf("*" to 3, "/" to 3, "+" to 2, "-" to 2, "(" to 4, ")" to 4)
    private val equal = mapOf("*" to 2, "/" to 2, "+" to 2, "-" to 2, "(" to 4, ")" to 4)
    private val plusGreaterThanMultiply = mapOf("*" to 2, "/" to 2, "+" to 3, "-" to 3, "(" to 4, ")" to 4)

    private fun shuntingYards(tokens: List<String>, precedence: Map<String, Int>) : List<String> {
        val outputQueue: Queue<String> = LinkedList(listOf())
        val operatorStack: Stack<String> = Stack()

        tokens.forEach{ t->
           if (t.toLongOrNull() != null) {
               outputQueue.add(t)
           }
           else if (allOperators.contains(t)) {
               while (
                   !operatorStack.isEmpty() &&
                   precedence[operatorStack.peek()!!]!! >= precedence[t]!! &&
                   operatorStack.peek() != "("
               ) {
                    outputQueue.add(operatorStack.pop())
               }
               operatorStack.push(t)
           }
           else if (t == "(") {
               operatorStack.push("(")
           }
           else if (t == ")") {
               while (
                   operatorStack.peek()!! != "("
               ) {
                   outputQueue.add(operatorStack.pop())
               }
               if (operatorStack.peek()!! == "(") {
                   operatorStack.pop()
               }
           }
        }
        while(!operatorStack.isEmpty())
            outputQueue.add(operatorStack.pop())

        return outputQueue.toList()
    }

    private fun evaluateRPN(tokens: List<String>): Long {
        return tokens.fold(Stack<Long>()) { acc, t ->
            if (allOperators.contains(t)) {
                val n1 = acc.pop()!!
                val n2 = acc.pop()!!
                acc.push(performOperation(t, n1, n2))
                acc
            }
            else {
                acc.push(t.toLong())
                acc
            }
        }.pop()!!
    }

    private fun performOperation(operation: String, n1: Long, n2: Long): Long {
        return when (operation) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            "/" -> n1 / n2
            else -> throw UnsupportedOperationException()
        }
    }

    private fun splitIntoToken(s: String): List<String> {
        val result = s.fold(Pair(listOf<String>(), "")) { acc, c ->
            when (c) {
                ' ' -> acc
                '(' -> processLastEntryInAccumulator(acc, '(')
                ')' -> processLastEntryInAccumulator(acc, ')')
                '*' -> processLastEntryInAccumulator(acc, '*')
                '+' -> processLastEntryInAccumulator(acc, '+')
                else -> {
                    Pair(acc.first, acc.second + c)
                }
            }
        }

        return (result.first + result.second).filter { it != ""}
    }

    private fun processLastEntryInAccumulator(p: Pair<List<String>, String>, c: Char): Pair<List<String>, String> {
        return Pair(p.first + p.second + c.toString(), "")
    }
}

class Stack<T>{
    private val elements: MutableList<T> = mutableListOf()
    fun isEmpty() = elements.isEmpty()
    fun count() = elements.size
    fun push(item: T) = elements.add(item)
    fun pop() : T? {
        val item = elements.lastOrNull()
        if (!isEmpty()){
            elements.removeAt(elements.size -1)
        }
        return item
    }
    fun peek() : T? = elements.lastOrNull()

    override fun toString(): String = elements.toString()
}

fun <T> Stack<T>.push(items: Collection<T>) = items.forEach { this.push(it) }

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
            var x = parseTerm()
            while (true) {
                when {
                    eat(ADD) -> x += parseTerm()
                    eat(MULTIPLY) -> x *= parseTerm()
                    else -> return x
                }
            }
        }

        fun parseTerm(): Long {
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
            var x = parseTerm()
            while (true) {
                when {
                    eat(ADD) -> x += parseTerm()
                    eat(MULTIPLY) -> x *= parseExpression()
                    else -> return x
                }
            }
        }

        fun parseTerm(): Long {
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