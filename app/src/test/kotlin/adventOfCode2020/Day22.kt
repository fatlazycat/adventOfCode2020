package adventOfCode2020

import org.junit.Test

class Day22 {
    @Test
    fun testPart1() {
        val p1 = player1.lines().map { it.toLong() }
        val p2 = player2.lines().map { it.toLong() }
        val game = playGame(p1, p2)
        val result = game.reversed().mapIndexed{ index, i -> (index+1) * i }.sum()

        assert(result == 33694L)
    }

    @Test
    fun testPart1test() {
        val p1 = player1Test.lines().map { it.toLong() }
        val p2 = player2Test.lines().map { it.toLong() }
        val game = playGame(p1, p2)
        val result = game.reversed().mapIndexed{ index, i -> (index+1) * i }.sum()

        assert(result == 306L)
    }

    @Test
    fun testPart2() {
        val p1 = player1.lines().map { it.toLong() }
        val p2 = player2.lines().map { it.toLong() }
        val game = playGameRecursiveCombat(p1, p2, listOf()).second
        val result = game.reversed().mapIndexed{ index, i -> (index+1) * i }.sum()

        assert(result == 31835L)
    }

    @Test
    fun testPart2test() {
        val p1 = player1Test.lines().map { it.toLong() }
        val p2 = player2Test.lines().map { it.toLong() }
        val game = playGameRecursiveCombat(p1, p2, listOf()).second
        val result = game.reversed().mapIndexed{ index, i -> (index+1) * i }.sum()

        assert(result == 291L)
    }

    private tailrec fun playGameRecursiveCombat(p1: List<Long>, p2: List<Long>, previous: List<Pair<List<Long>, List<Long>>>): Pair<Boolean, List<Long>> {
        return when {
            p1.isEmpty() -> {
                Pair(false, p2)
            }
            p2.isEmpty() -> {
                Pair(true, p1)
            }
            previousRoundMatch(Pair(p1, p2), previous) ->{
                Pair(true, p1)
            }
            else -> {
                val p1Top = p1.first()
                val p2Top = p2.first()
                val p1Remaining = p1.drop(1)
                val p2Remaining = p2.drop(1)

                if(p1Remaining.size >= p1Top && p2Remaining.size >= p2Top) {
                    val resultOfSubGame = playGameRecursiveCombat(p1Remaining.take(p1Top.toInt()), p2Remaining.take(p2Top.toInt()), listOf())

                    if(resultOfSubGame.first)
                        playGameRecursiveCombat(p1Remaining + p1Top + p2Top, p2Remaining, previous + Pair(p1, p2))
                    else
                        playGameRecursiveCombat(p1Remaining, p2Remaining + p2Top + p1Top, previous + Pair(p1, p2))

                } else {
                    if(p1Top > p2Top)
                        playGameRecursiveCombat(p1Remaining + p1Top + p2Top, p2Remaining, previous + Pair(p1, p2))
                    else
                        playGameRecursiveCombat(p1Remaining, p2Remaining + p2Top + p1Top, previous + Pair(p1, p2))
                }
            }
        }
    }

    @Test
    fun testPreviousRoundMatch() {
        val toMatch = Pair(listOf(2L,3L,4L,5L), listOf(7L,8L,9L,10L))
        val previousRounds = listOf(Pair(listOf(1L,2L,3L,4L,5L), listOf(6L,7L,8L,9L,10L)), Pair(listOf(2L,3L,4L,5L), listOf(7L,8L,9L,10L)))

        assert(previousRoundMatch(toMatch, previousRounds))
    }

    private fun previousRoundMatch(current: Pair<List<Long>, List<Long>>, previous: List<Pair<List<Long>, List<Long>>>): Boolean {
        return previous.contains(current)
    }

    private tailrec fun playGame(p1: List<Long>, p2: List<Long>): List<Long> {
        return if(p1.isEmpty())
            p2
        else if (p2.isEmpty())
            p1
        else {
            if(p1.first() > p2.first())
                playGame(p1.drop(1) + p1.first() + p2.first(), p2.drop(1))
            else
                playGame(p1.drop(1), p2.drop(1) + p2.first() + p1.first())

        }
    }

    private val player1Test = """
        9
        2
        6
        3
        1
    """.trimIndent()

    private val player2Test = """
        5
        8
        4
        7
        10
    """.trimIndent()

    private val player1 = """
        28
        13
        25
        16
        38
        3
        14
        6
        29
        2
        47
        20
        35
        43
        30
        39
        21
        42
        50
        48
        23
        11
        34
        24
        41
    """.trimIndent()

    private val player2 = """
        27
        37
        9
        10
        17
        31
        19
        33
        40
        12
        32
        1
        18
        36
        49
        46
        26
        4
        45
        8
        15
        5
        44
        22
        7
    """.trimIndent()
}