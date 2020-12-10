package adventOfCode2020

import org.junit.Test

class Day10 {
    @Test
    fun testPart1() {
        val lines = getFileAsListOfLines("/day10")
        val data = lines.map { it.toInt() }
        val sortedData = data.sorted()
        val d = findChain(sortedData + listOf(sortedData.maxOrNull()!!+3))

        assert(d.one * d.three == 1998)
    }

    @Test fun testPart2() {
        val lines = getFileAsListOfLines("/day10test2")
        val data = lines.map { it.toInt() }
        val sortedData = data.sorted()
        val total = findAllChains(sortedData + listOf(sortedData.maxOrNull()!!+3))
        println(total)
    }

    private fun findAllChains(l: List<Int>) : Int {
        val finalNum = l.last()
        val body = l.take(l.size-1)
        var total = mutableListOf<List<Int>>()

        val indices = (0..body.size-1).windowed(3)

        indices.map { i ->
            val head = body.subList(0, i[0])
            val tail = body.subList(i[2], body.size)

            if(findChain(head + body[i[0]] + tail + finalNum).last != -1)
                total.add(head + body[i[0]] + tail + finalNum)

            if(findChain(head + body[i[1]] + tail + finalNum).last != -1)
                total.add(head + body[i[1]] + tail + finalNum)

            if(findChain(head + body[i[2]] + tail + finalNum).last != -1)
                total.add(head + body[i[2]] + tail + finalNum)

            if(findChain(head + body[i[0]] + body[i[1]] + tail + finalNum).last != -1)
                total.add(head + body[i[0]] + body[i[1]] + tail + finalNum)

            if(findChain(head + body[i[1]] + body[i[2]] + tail + finalNum).last != -1)
                total.add(head + body[i[1]] + body[i[2]] + tail + finalNum)

            if(findChain(head + body[i[0]] + body[i[2]] + tail + finalNum).last != -1)
                total.add(head + body[i[0]] + body[i[2]] + tail + finalNum)

            if(findChain(head + body[i[0]] + body[i[1]] + body[i[2]] + tail + finalNum).last != -1)
                total.add(head + body[i[0]] + body[i[1]] + body[i[2]] + tail + finalNum)
        }

        return total.distinct().size
    }

    private fun findChain(l: List<Int>) : Data {
        return l.fold(Data(0,0,0,0)) { acc, i ->
            processEntry(i, acc)
        }
    }

    private fun processEntry(i: Int, acc: Data) : Data {
        return when (i) {
            acc.last + 1 -> Data(i, acc.one + 1, acc.two, acc.three)
            acc.last + 2 -> Data(i, acc.one, acc.two+1, acc.three)
            acc.last + 3 -> Data(i, acc.one, acc.two, acc.three+1)
            else -> Data(-1, -1, -1, -1)
        }
    }

    private data class Data(val last: Int, val one: Int, val two: Int, val three: Int)
}

