import kotlin.math.abs

fun main() {
    data class SplitLists(val left: List<Int>, val right: List<Int>)
    fun listSplit(input: List<String>): SplitLists {
        val lefts = mutableListOf<Int>()
        val rights = mutableListOf<Int>()
        input.forEach {
            val parts = it.split(" ").filter { it !=  "" }
            lefts += Integer.parseInt(parts[0])
            rights += Integer.parseInt(parts[1])
        }
        return SplitLists(lefts, rights)
    }

    fun part1(input: List<String>): Int {
        val split = listSplit(input)
        val lefts = split.left.toMutableList()
        val rights = split.right.toMutableList()

        lefts.sort()
        rights.sort()

        var sum = 0
        lefts.forEachIndexed { index, i ->
            sum += abs(i-rights[index])
        }
        return sum


    }

    fun part2(input: List<String>): Int {
        val (lefts, rights) = listSplit(input)
        var score = 0
        for(l in lefts){
            score += l*rights.filter{it == l}.size
        }
        return score
    }

    val sampleInput = readInput("Day01_s")
    val testInput = readInput("Day01")
    part1(testInput).println()

    part2(testInput).println()
}
