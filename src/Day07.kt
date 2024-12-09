import java.math.BigInteger

fun main() {

    fun List<String>.processOperators(): BigInteger {
        var sum = this[0].toBigInteger()
        this.forEachIndexed { index, item ->
            if (index > 0 && index % 2 == 0){
                when(this[index -1]) {
                    "+" -> sum += item.toBigInteger()
                    "*" -> sum *= item.toBigInteger()
                    "|" -> sum = (sum.toString()+item).toBigInteger()
                }
            }
        }
        return sum
    }

    fun List<String>.GetOperatorPerms(withConcat: Boolean =false): List<List<String>>{
        var operatorPerms = listOf<List<String>>(this)

        for (i in 1..<(this.size*2)-1 step 2){
            val newOperatorPerms = mutableListOf<List<String>>()
            operatorPerms.forEach {
                val plusList = it.toMutableList()
                val multList = it.toMutableList()
                plusList.add(i, "+")
                multList.add(i, "*")
                newOperatorPerms += plusList
                newOperatorPerms += multList
                if (withConcat) {
                    val concatList = it.toMutableList()
                    concatList.add(i, "|")
                    newOperatorPerms += concatList
                }
            }
            operatorPerms = newOperatorPerms
        }
        return operatorPerms
    }

    fun part1(input: List<String>): BigInteger {
        var result = 0.toBigInteger()
        input.forEach{
            val total = it.split(":")[0].toBigInteger()
            val numbers = it.split(":")[1].split(" ").filter { it != "" }
            if (numbers.GetOperatorPerms().any { it.processOperators() == total }) {
                result += total
            }
        }
        return result
    }

    fun part2(input: List<String>): Any {
        var result = 0.toBigInteger()
        input.forEach{
            val total = it.split(":")[0].toBigInteger()
            val numbers = it.split(":")[1].split(" ").filter { it != "" }
            if (numbers.GetOperatorPerms(true).any { it.processOperators() == total }) {
                result += total
            }
        }
        return result
    }

    val sampleInput = readInput("Day07_s")
    val testInput = readInput("Day07")
    part1(testInput).println()
    part2(testInput).println()
}