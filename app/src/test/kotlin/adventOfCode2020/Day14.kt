package adventOfCode2020

import com.google.common.math.LongMath.pow
import org.junit.Test
import java.lang.UnsupportedOperationException

class Day14 {

    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day14")
        val data = lines.map { l ->
            val s = l.split("=")
            Pair(s[0].trim(), s[1].trim())
        }
        val mask = (1..36).map { 'X' }.joinToString("")
        val memory = processData(data, mutableMapOf(), mask)
        val total = memory.values.sum()

        assert(total == 12135523360904L)
    }

    private tailrec fun processData(
        data: List<Pair<String, String>>,
        memory: MutableMap<Long, Long>,
        mask: String
    ): MutableMap<Long, Long> {
        return when {
            data.isEmpty() -> {
                memory
            }
            data.first().first == "mask" -> {
                processData(data.drop(1), memory, data.first().second)
            }
            else -> { // process memory
                val memAddress = getMemoryAddress(data.first().first)
                val processedNumber = processMemory(data.first().second.toLong(), mask)

                memory[memAddress] = processedNumber
                processData(data.drop(1), memory, mask)
            }
        }
    }

    private fun processMemory(n: Long, mask: String): Long {
        val b = toBinary(n)
        val applied = mask.mapIndexed { index, fn ->
            when (fn) {
                'X' -> b[index]
                '1' -> '1'
                '0' -> '0'
                else -> throw UnsupportedOperationException()
            }
        }
        val appliedAsString = applied.joinToString("")

        return toDecimal(appliedAsString)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day14")
        val data = lines.map { l ->
            val s = l.split("=")
            Pair(s[0].trim(), s[1].trim())
        }

        val memory = processData2(data, mutableMapOf(), "")
        val total = memory.values.sum()

        assert(total == 2741969047858)
    }

    private tailrec fun processData2(
        data: List<Pair<String, String>>,
        memory: MutableMap<Long, Long>,
        mask: String
    ): MutableMap<Long, Long> {
        return when {
            data.isEmpty() -> {
                memory
            }
            data.first().first == "mask" -> {
                processData2(data.drop(1), memory, data.first().second)
            }
            else -> { // process memory
                val memAddress = getMemoryAddress(data.first().first).toLong()
                val memAddressAsBinary = toBinary(memAddress)
                val memAddressAfterMask = processMemoryBinary(memAddressAsBinary, mask)
                val allAddress = getAllAddresses(memAddressAfterMask.toList(), listOf())
                val allAddressAsString = allAddress.map { it.joinToString("") }
                val allAddressesAsLong = allAddressAsString.map { toDecimal(it) }
                val processedNumber = data.first().second.toLong()

                allAddressesAsLong.map { memory[it] = processedNumber }

                processData2(data.drop(1), memory, mask)
            }
        }
    }

    @Test
    fun testMaskGeneration() {
        val mask = "000000000000000000000000000000X1001X"
        val address = "000000000000000000000000000000101010"
        val processedMask = processMemoryBinary(address, mask)
        val allMask = getAllAddresses(processedMask.toList(), listOf())
        val allMaskAsString = allMask.map { it.joinToString("") }
        val expected = listOf(
            "000000000000000000000000000000011010",
            "000000000000000000000000000000011011",
            "000000000000000000000000000000111010",
            "000000000000000000000000000000111011"
        )

        assert(allMaskAsString == expected)
    }

    private fun getAllAddresses(mask: List<Char>, newMask: List<Char>): List<List<Char>> {
        return when {
            mask.isEmpty() -> {
                listOf(newMask)
            }
            mask.first() == 'X' -> {
                listOf(
                    getAllAddresses(mask.drop(1), newMask + '0') + getAllAddresses(
                        mask.drop(1),
                        newMask + '1'
                    )
                ).flatten()
            }
            else -> {
                listOf(getAllAddresses(mask.drop(1), newMask + mask.first())).flatten()
            }
        }
    }

    private fun processMemoryBinary(b: String, mask: String): String {
        val applied = mask.mapIndexed { index, fn ->
            if (fn == 'X')
                'X'
            else if (fn == '0' && b[index] == '0')
                '0'
            else '1'
        }

        return applied.joinToString("")
    }

    @Test
    fun testBinaryConversions() {
        assert(toBinary(73L) == "000000000000000000000000000001001001")
        assert(toDecimal("000000000000000000000000000001001001") == 73L)
        assert(toDecimal("110010101010000110110101010000001100") == 54393525260L)
    }

    private fun toDecimal(b: String): Long {
        return b.reversed().mapIndexed { k, v ->
            v.toString().toLong() * pow(2, k)
        }.sum()
    }

    private fun toBinary(d: Long, s: String = ""): String {
        while (d > 0) {
            val temp = "${s}${d % 2}"
            return toBinary(d / 2, temp)
        }
        return s.reversed().padStart(36, '0')
    }

    @Test
    fun testMemoryAddress() {
        assert(getMemoryAddress("mem[8]") == 8L)
    }

    private val regex = """.*\[(\d*)\].*""".toRegex()

    private fun getMemoryAddress(s: String): Long {
        regex.find(s)!!.groupValues.let { (_, s) ->
            return s.toLong()
        }
    }
}