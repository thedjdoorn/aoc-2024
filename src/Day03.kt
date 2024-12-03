import java.lang.Integer.parseInt
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val regex =  """mul\((?<first>([0-9]){1,3}),(?<second>([0-9]){1,3})\)""".toRegex()

        var sum = 0;

        input.forEach {
            val matches = regex.findAll(it).toList()
            matches.forEach {
                val f = parseInt(it.groups["first"]?.value)
                val s = parseInt(it.groups["second"]?.value)
                //println( "$f * $s = ${f*s}")
                sum += f*s
            }
        }
        return sum;
    }

    fun part2(input: List<String>): Int {
        val regex =  """mul\((?<first>([0-9]){1,3}),(?<second>([0-9]){1,3})\)""".toRegex()
        val enableRegex = """do(n't)?\(\)""".toRegex()

        var sum = 0

        var canDo = true
        input.forEach{
            val matches = regex.findAll(it).toMutableList()
            matches += enableRegex.findAll(it).toMutableList()
            matches.sortBy {
                it.range.start
            }
            matches.forEach{
                if (it.value in listOf("do()", "don't()")){
                    canDo = it.value == "do()"
                    //println("Switchted dovalue to $canDo")
                } else if (canDo){
                    val f = parseInt(it.groups["first"]?.value)
                    val s = parseInt(it.groups["second"]?.value)
                    //println( "$f * $s = ${f*s}")
                    sum += f*s
                } else {
                    //println("Skipped ${it.value}")
                }
            }
        }
        return sum
    }

    val sampleInput = readInput("Day03_s")
    val secondSampleInput = readInput("Day03_s2")
    val testInput = readInput("Day03")

    part1(testInput).println()

    part2(testInput).println()
}
