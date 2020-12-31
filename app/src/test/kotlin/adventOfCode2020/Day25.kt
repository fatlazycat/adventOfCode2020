package adventOfCode2020

import org.junit.Test

class Day25 {

    private val cardPublicKey = 18356117L
    private val doorPublicKey = 5909654L
    private val cardPublicKeyTest = 5764801L
    private val doorPublicKeyTest = 17807724L

    @Test
    fun testPart1test() {
        assert(findLoopSize(cardPublicKeyTest) == 8)
        assert(findLoopSize(doorPublicKeyTest) == 11)
        assert(findLoopSize(cardPublicKey) == 3974372)
        assert(findLoopSize(doorPublicKey) == 8623737)

        assert(transform(doorPublicKeyTest, 8) == 14897079L)
        assert(transform(cardPublicKeyTest, 11) == 14897079L)
        assert(transform(doorPublicKey, 3974372) == 16902792L)
        assert(transform(cardPublicKey, 8623737) == 16902792L)
    }

    private fun findLoopSize(target: Long): Int =
        generateSequence(1L) { loopSize -> calc(7, loopSize) }.indexOf(target)

    private fun calc(subjectNumber: Long, loopSize: Long): Long = (subjectNumber * loopSize) % 20201227L

    private fun transform(subject: Long, loopSize: Int): Long =
        generateSequence(1L) { calc(subject, it) }.drop(loopSize).first()
}
