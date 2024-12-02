import kotlin.math.abs

fun main() {

    class Sequence(input: String) {
        var sequence: List<Int> = input.split( " ").map { Integer.parseInt(it) }.toMutableList()

        fun isOrdered(): Boolean {
            if (sequence.size < 2) return true
            val ascending = sequence[0] < sequence[1]
            for (i in 1..<sequence.size){
                if ((ascending && sequence[i]<sequence[i-1]) || (!ascending && sequence[i]>sequence[i-1])) {
                    return false
                }
            }
            return true
        }

        fun isLeveled(): Boolean{
            if (sequence.size < 2) return true
            for(i in 1..<sequence.size){
                val diff = abs(sequence[i]-sequence[i-1])
                if (diff < 1 || diff > 3){
                    return false
                }
            }
            return true
        }

        fun canBeDampened(): Boolean {
            val originalSequence = sequence
            for(i in 0..<sequence.size) {
                sequence = sequence.filterIndexed{index,_ -> index != i}
                if(isSafe()){
                    sequence = originalSequence
                    println("sequence $sequence can be dampened by removing ${sequence[i]}(index $i)")
                    return true
                } else {
                    sequence = originalSequence
                }
            }
            return false
        }

        fun isSafe(): Boolean {
            return isOrdered() && isLeveled()
        }
    }

    fun part1(input: List<String>): Int {
        return input.filter { Sequence(it).isSafe() }.size
    }

    fun part2(input: List<String>): Int {
        return input.filter { Sequence(it).isSafe() || Sequence(it).canBeDampened() }.size
    }

    val sampleInput = readInput("Day02_s")
    val testInput = readInput("Day02")

    part1(testInput).println()

    part2(testInput).println()
}
