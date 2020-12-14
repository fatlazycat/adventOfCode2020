package adventOfCode2020

import com.google.common.math.IntMath.pow
import org.junit.Test

class Day14 {
    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day14test")
        val data = lines.map{ l ->
            val s = l.split("=")
            Pair(s[0].trim(), s[1].trim())
        }

//        println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X")
        println(toBinary(73L))

//        println(data)
    }

    @Test
    fun testBinaryConversions(){
        assert(toBinary(73L) == "1001001")
        assert(toDecimal("1001001") == 73L)
    }

    private fun toDecimal(b : String) : Long {
        var sum = 0L
        b.reversed().forEachIndexed {
                k, v -> sum += v.toString().toInt() * pow(2, k)
        }
        return sum
    }

    private fun toBinary(d: Long, s: String = "") : String {
        while (d > 0) {
            val temp = "${s}${d%2}"
            return toBinary(d/2, temp)
        }
        return s.reversed()
    }

//    private tailrec fun processData(data: List<Pair<String, String>>, memory: Map<Int, Long>, mask: Long): Map<Int, Long> {
//        if(data.isEmpty())
//            return memory
//        else if data.first() == "mask" {
//            return processData(data.drop(1), memory, )
//        }
//
//    }
}