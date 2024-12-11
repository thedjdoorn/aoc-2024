import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {

    fun ProcessStone(stone: String):List<String>{
        if (stone == "0") return listOf("1")
        if (stone.length%2 ==0) {
            val result = mutableListOf<String>()
            result.add(stone.take(stone.length/2).toBigInteger().toString())
            result.add(stone.takeLast(stone.length/2).toBigInteger().toString())
            return result
        }
        return listOf((stone.toBigInteger()*2024.toBigInteger()).toString())
    }

    fun ProcessStones(stones: List<String>): List<String>{
        val result = mutableListOf<String>()
        runBlocking {
            stones.forEach {
                result += ProcessStone(it)
            }
        }
        return result
    }

    fun RecursiveProcessStones(stones: List<String>, currentIter: Int, targetIter: Int):BigInteger{
        var cIter = currentIter
        if (currentIter == targetIter){
            return 1.toBigInteger()
        }
        if (currentIter == 5 || currentIter == 74) {
            println("Doing a lvl $currentIter iteration")
        }
        var result = BigInteger.ZERO
            var s = ProcessStones(stones)
            while (s.size < 500 && cIter < targetIter-1) {
                s = ProcessStones(s)
                cIter++
            }
        "Skipped from $currentIter to $cIter".println()
            s.forEach {
                result += RecursiveProcessStones(listOf(it), cIter+1, targetIter)
            }
        return result
    }

    fun part1(input: String): Int {
        var stones = input.split(" ").filter { it.isNotBlank() }
        for (i in 0..24){
            val time = measureTimeMillis {
                stones = ProcessStones(stones)
            }
            "Did iteration $i in $time ms".println()
        }
        return stones.size
    }

    fun part2(input: String): BigInteger {
        var stones = input.split(" ").filter { it.isNotBlank() }
        var count = BigInteger.ZERO
        val time = measureTimeMillis {
            count = RecursiveProcessStones(stones, 0, 75)
        }
        "Ran in $time ms".println()
        return count
    }

    val sampleInput = readInput("Day11_s")[0]
    val testInput = readInput("Day11")[0]
//    part1(testInput).println()
    part2(testInput).println()
}