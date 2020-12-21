package adventOfCode2020

import org.junit.Test

class Day21 {

    @Test
    fun testPart1() {
//        val lines: List<String> = getFileAsListOfLines("/day21test")
        val lines: List<String> = getFileAsListOfLines("/day21")
        val data = processData(lines)
        val allergensToIngredients = getAllergensToIngredients(data)
        val ingredientsToAllergens = getIngredientsToAllergens(data)
        val matches = findMatches(allergensToIngredients, ingredientsToAllergens)
        val allAllergens = data.toMap().values.flatten().distinct()

        if(matches.size != allAllergens.size) {
            println("match not complete")
        }

        val allIngredients = data.toMap().keys.flatten().distinct()
        val noAllergens = allIngredients.toSet() - matches.keys
        val allIngredientsWithDupes = data.map { it.first.toSet() }
        val ingredientsWithNoAllergens = allIngredientsWithDupes.map { it.intersect(noAllergens)}
        val total = ingredientsWithNoAllergens.map{ it.count() }.sum()

        println(total)
    }

    private fun findMatches(
        allergensToIngredients: Map<String, Map<String, Int>>,
        ingredientsToAllergens: Map<String, Map<String, Int>>
    ): Map<String, String> {
        val ingredientsToAllergensMutable = ingredientsToAllergens.entries.map { kv ->
            kv.key to kv.value.toMutableMap()
        }.toList().toMap().toMutableMap()
        val allergensToIngredientsMutable = allergensToIngredients.entries.map { kv ->
            kv.key to kv.value.toMutableMap()
        }.toList().toMap().toMutableMap()
        var stop = false
        val results = mutableMapOf<String, String>()

        while (!stop) {
            val definiteMatches = definiteMatchFromIngredients(ingredientsToAllergensMutable)

            if(definiteMatches.isNotEmpty()) {
                for (definiteMatch in definiteMatches) {
                    ingredientsToAllergensMutable.remove(definiteMatch.first)
                    results[definiteMatch.first] = definiteMatch.second
                }
                for (definiteMatch in definiteMatches) {
                    ingredientsToAllergensMutable.entries.map { kv ->
                        kv.value.remove(definiteMatch.second)
                    }
                }
            }
            else {
                stop = true
            }
        }

        for(r in results) {
            allergensToIngredientsMutable.remove(r.value)
        }
        for (r in results) {
            allergensToIngredientsMutable.entries.map { kv ->
                kv.value.remove(r.key)
            }
        }

        for( a in allergensToIngredientsMutable.entries) {
            val l = a.value.toList()

            if(l.size == 1)
                results[l.first().first] = a.key
        }

        return results
    }

    private fun definiteMatchFromIngredients(ingredientsToAllergens: Map<String, Map<String, Int>>): List<Pair<String, String>> {
        return ingredientsToAllergens.entries.map { kv ->
            val max = kv.value.maxByOrNull { it.value }

            if(max != null) {
                val entries = kv.value.filter { it.value == max.value }

                if (entries.size == 1 && max.value > 1)
                    kv.key to entries.toList().first().first
                else
                    null
            }
            else {
                null
            }
        }.filterNotNull()
    }

    private fun getAllergensToIngredients(list: List<Pair<List<String>, List<String>>>): Map<String, Map<String, Int>> {
        val allergensToIngredients = list.map { p ->
            p.second.map { a ->
                a to p.first
            }
        }.flatten()

        val possible = allergensToIngredients.groupBy { it.first }
        val listCounts = possible.map { p ->
            p.key to p.value.map { it.second }.flatten()
        }
        val counts = listCounts.map { pair ->
            pair.first to pair.second.groupingBy { it }.eachCount()
        }

        return counts.toMap()
    }

    private fun getIngredientsToAllergens(list: List<Pair<List<String>, List<String>>>): Map<String, Map<String, Int>> {
        val ingredientsToAllergens = list.map { p ->
            p.first.map { a ->
                a to p.second
            }
        }.flatten()

        val possible = ingredientsToAllergens.groupBy { it.first }
        val listCounts = possible.map { p ->
            p.key to p.value.map { it.second }.flatten()
        }
        val counts = listCounts.map { pair ->
            pair.first to pair.second.groupingBy { it }.eachCount()
        }

        return counts.toMap()
    }

    private fun processData(list: List<String>): List<Pair<List<String>, List<String>>> {
        val regex = """(.*)\(contains (.*)\)""".toRegex()
        return list.map { l ->
            regex.find(l)!!.groupValues.let { (_, i, a) ->
                val ingredients = i.trim().split(" ")
                val allergens = a.trim().replace(",", "").split(" ")
                Pair(ingredients, allergens)
            }
        }
    }
}