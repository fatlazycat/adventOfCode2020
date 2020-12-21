package adventOfCode2020

import org.junit.Test

class Day21 {

    @Test
    fun testPart1() {
        val lines: List<String> = getFileAsListOfLines("/day21")
        val unassociatedCount = getCountOfNonAllergens(lines)

        assert(unassociatedCount == 2230)
    }

    @Test
    fun testPart1test() {
        val lines: List<String> = getFileAsListOfLines("/day21test")
        val unassociatedCount = getCountOfNonAllergens(lines)

        assert(unassociatedCount == 5)
    }

    @Test
    fun testPart2() {
        val lines: List<String> = getFileAsListOfLines("/day21")
        val answer = getSortedIngredients(lines)

        assert(answer == "qqskn,ccvnlbp,tcm,jnqcd,qjqb,xjqd,xhzr,cjxv")
    }

    @Test
    fun testPart2test() {
        val lines: List<String> = getFileAsListOfLines("/day21test")
        val answer = getSortedIngredients(lines)

        println(answer == "mxmxvkd,sqjhc,fvjkl")
    }

    private fun getCountOfNonAllergens(lines: List<String>): Int {
        val ingredientsToAllergens = processData(lines)
        val ingredients = ingredientsToAllergens.flatMap { p -> p.first }.distinct().toSet()
        val allergens = ingredientsToAllergens.flatMap { p -> p.second }.distinct().toSet()
        val commonIngredientsByAllergen = getCommonIngredientsByAllergen(allergens, ingredientsToAllergens)
        val allergensInCommonIngredientsByAllergen = commonIngredientsByAllergen.values.reduce { a, b -> a union b }
        val allergensNotInCommonIngredientsByAllergen = ingredients - allergensInCommonIngredientsByAllergen

        return ingredientsToAllergens.map { (ingredients, _) ->
            ingredients.count { it in allergensNotInCommonIngredientsByAllergen }
        }.sum()
    }

    private fun getSortedIngredients(lines: List<String>): String {
        val ingredientsToAllergens = processData(lines)
        val ingredients = ingredientsToAllergens.flatMap { p -> p.first }.distinct().toSet()
        val allergens = ingredientsToAllergens.flatMap { p -> p.second }.distinct().toSet()
        val commonIngredientsByAllergen = getCommonIngredientsByAllergen(allergens, ingredientsToAllergens)
        val allergensInCommonIngredientsByAllergen = commonIngredientsByAllergen.values.reduce { a, b -> a union b }
        val allergensNotInCommonIngredientsByAllergen = ingredients - allergensInCommonIngredientsByAllergen
        val allergenCandidates =
            getAllergenCandidates(allergens, ingredientsToAllergens, allergensNotInCommonIngredientsByAllergen)
        val result = getAllergenTranslation(allergenCandidates)
        val answer = result.entries.sortedBy { (a, _) -> a }.joinToString(",") { (_, i) -> i }
        return answer
    }

    private tailrec fun getAllergenTranslation(
        allergenCandidates: Map<String, Set<String>>,
        current: Map<String, String> = mapOf()
    ): Map<String, String> {
        return if (allergenCandidates.isEmpty()) {
            current
        } else {
            val exactMatch = allergenCandidates.filterValues { it.size == 1 }.mapValues { (_, s) -> s.first() }
            val remainingAllergens = allergenCandidates.mapValues { (_, ingredients) ->
                ingredients - exactMatch.values.toSet() }
                .filterValues { it.isNotEmpty() }
            val newResults = current + exactMatch

            getAllergenTranslation(remainingAllergens, newResults)
        }
    }

    private fun getAllergenCandidates(
        allergens: Set<String>,
        ingredientsToAllergens: List<Pair<Set<String>, Set<String>>>,
        allergensNotInCommonIngredientsByAllergen: Set<String>
    ) = allergens.asSequence().mapNotNull { allergen ->
        val i = ingredientsToAllergens.asSequence()
            .filter { it.second.contains(allergen) }
            .map { it.first - allergensNotInCommonIngredientsByAllergen }
            .reduce { a, b -> a intersect b }
        if (i.isEmpty()) null else allergen to i
    }.toMap()

    private fun getCommonIngredientsByAllergen(
        allergens: Set<String>,
        ingredientsToAllergens: List<Pair<Set<String>, Set<String>>>
    ) = allergens.asSequence().map { allergen ->
        allergen to ingredientsToAllergens.asSequence()
            .filter { it.second.contains(allergen) }
            .map { it.first }
            .reduce { a, b -> a intersect b }
    }.toMap()


    private fun processData(list: List<String>): List<Pair<Set<String>, Set<String>>> {
        val regex = """(.*)\(contains (.*)\)""".toRegex()
        return list.map { l ->
            regex.find(l)!!.groupValues.let { (_, i, a) ->
                val ingredients = i.trim().split(" ")
                val allergens = a.trim().replace(",", "").split(" ")
                Pair(ingredients.toSet(), allergens.toSet())
            }
        }
    }
}