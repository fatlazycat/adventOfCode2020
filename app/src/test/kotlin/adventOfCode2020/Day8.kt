package adventOfCode2020

import org.junit.Test

enum class Op {
    NOP,
    ACC,
    JMP
}

class Day8 {
    @Test
    fun day8() {
        val data = getFileAsListOfLines("/day8")
        val dataList = makeData(data)
        assert(followPath(dataList) == Pair(false, 1654))
    }

    @Test
    fun day8Part2() {
        val data = getFileAsListOfLines("/day8")
        val dataList = makeData(data)
        assert(changeJmpToNop(dataList) == Pair(true, 833))

        // Note does not deal with changing a NOP to a JMP but didn't need it to solve issue
    }

    private fun changeJmpToNop(data: List<Pair<Op, Int>>): Pair<Boolean, Int> {
        val indices = data.mapIndexed { i, e -> if (e.first == Op.JMP) i else -1 }.filter { it != -1 }

        for (i in indices) {
            var copyData = data.toMutableList()
            copyData[i] = Pair(Op.NOP, 0)

            val potentialResult = followPath(copyData)
            if (potentialResult.first)
                return potentialResult
        }

        return Pair(false, 0)
    }

    private fun followPath(data: List<Pair<Op, Int>>): Pair<Boolean, Int> {
        var acc = 0
        var entriesDone = mutableListOf<Int>()
        var currentStep = 1

        while (!entriesDone.contains(currentStep)) {
            if (currentStep == data.size) {
                return Pair(true, acc)
            }
            else if (currentStep > data.size || currentStep < 0)
                return Pair(false, -1)

            val p = data[currentStep]!!

            entriesDone.add(currentStep)

            when (p.first) {
                Op.NOP -> {
                    currentStep += 1
                }
                Op.ACC -> {
                    currentStep += 1
                    acc += p.second
                }
                Op.JMP -> {
                    currentStep += p.second
                }
            }
        }

        return Pair(!entriesDone.contains(currentStep), acc)
    }

    private fun makeData(l: List<String>): List<Pair<Op, Int>> {
        return l.map { i -> makeEntry(i) }
    }

    private fun makeEntry(s: String): Pair<Op, Int> {
        val parts = s.split(" ")
        return Pair(Op.valueOf(parts[0].toUpperCase()), parts[1].toInt())
    }
}