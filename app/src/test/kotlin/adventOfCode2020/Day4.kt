package adventOfCode2020

import kotlin.test.Test

class Day4 {
    @Test fun testDay4() {
        val testData: List<String> = getFileAsListOfLines("/day4")
        val splitPoints = testData.mapIndexed{ index, item -> if (item.isNullOrEmpty()) index else -1 }.filter { it != -1 }; val passportEntries =
            listOfListsByBlankLine(testData).map { i -> i.map{ j -> stringToEntry(j) }.toMap() }

        val number = passportEntries.map { e -> isPassportPresent(e) }.map { i -> if (i) 1 else 0 }.sum()
        assert(number == 202)

        val number2 = passportEntries.map { e -> isPassportPresent(e) && isPassportValid(e) }.map { i -> if (i) 1 else 0 }.sum()
        assert(number2 == 137)
    }

    private fun stringToEntry(s: String) : Pair<String, String> {
        val sp = s.split(":")
        return sp[0] to sp[1]
    }

    private val keysWithoutCid = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    private val keysWithCid = keysWithoutCid + "cid"

    private fun isPassportPresent(passport: Map<String, String>) : Boolean {
        return passport.keys == keysWithCid || passport.keys == keysWithoutCid
    }

    private fun isPassportValid(passport: Map<String, String>) : Boolean {
        return passport.getValue("byr").toInt() in 1920..2002 &&
                passport.getValue("iyr").toInt() in 2010..2020 &&
                passport.getValue("eyr").toInt() in 2020..2030 &&
                validHeight(passport.getValue("hgt")) &&
                validHairColour(passport.getValue("hcl")) &&
                validEyeColour(passport.getValue("ecl")) &&
                validPassportNumber(passport.getValue("pid"))
    }

    private fun validPassportNumber(pid: String): Boolean {
        return pid.matches("([0-9]{9})".toRegex())
    }

    private fun validEyeColour(ecl: String): Boolean {
        return ecl in listOf<String>("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    }

    private fun validHairColour(hcl: String): Boolean {
        return hcl.matches("#([0-9a-f]{6})".toRegex())
    }

    private fun validHeight(hgt: String) : Boolean {
        return when {
            hgt.endsWith("in") -> {
                val n = hgt.substring(0, hgt.length-2).toIntOrNull()

                n != null && n >= 59 && n <= 76
            }
            hgt.endsWith("cm") -> {
                val n = hgt.substring(0, hgt.length-2).toIntOrNull()

                n != null && n >= 150 && n <= 193
            }
            else -> {
                false
            }
        }
    }

//    byr (Birth Year) - four digits; at least 1920 and at most 2002.
//    iyr (Issue Year) - four digits; at least 2010 and at most 2020.
//    eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
//    hgt (Height) - a number followed by either cm or in:
//    If cm, the number must be at least 150 and at most 193.
//    If in, the number must be at least 59 and at most 76.
//    hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
//    ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
//    pid (Passport ID) - a nine-digit number, including leading zeroes.
//    cid (Country ID) - ignored, missing or not.
}
