import java.math.BigInteger

fun main() {

    fun GetDiskMapRepresentation(input:String): List<String> {
        val output = mutableListOf<String>()
        input.split("").filter{it.isNotBlank()}.forEachIndexed {index, character ->
            if (index % 2 == 0) {
                for(i in 0..<character.toInt()){
                    output += (index/2).toString()
                }
            } else {
                for(i in 0..<character.toInt()){
                    output += "."
                }
            }
        }
        return output
    }

    fun ReorderDiskMapRepresentation(input:List<String>):List<String> {
        val output = input.toMutableList()
        val deque = ArrayDeque<String>(output.filterNot { it == "." }.reversed())
        output.forEachIndexed {index, character ->
            if (deque.size > 0) {
                if (character == ".") {
                    output[index] = deque.removeFirst()
                } else {
                    deque.removeLast()
                }
            } else {
                output[index] = "."
            }
        }
        return output
    }

    fun GetGroups(input:List<String>): MutableList<MutableList<String>> {
        val groups = mutableListOf(mutableListOf<String>())
        input.forEach {
            if (groups.size >0 && groups.last().size>0 && groups.last().last() == it) {
                groups.last() += it
            } else {
                groups += mutableListOf(it)
            }
        }
        return groups.filter { it.size>0 }.toMutableList()
    }

    fun ReorderFiles(input:List<String>):List<String> {
        val groups = GetGroups(input)

        val reversed = groups.reversed()
        reversed.forEachIndexed replace@ { index, group ->
            if ("." !in group) {
                val indexOfRightElement = groups.indexOfLast { it[0] == group[0] }
                groups.forEachIndexed { i, g ->
                    if (i<indexOfRightElement && g.size >= group.size && "." in g) {
                        groups[indexOfRightElement] = mutableListOf()
                        repeat(group.size) {groups[indexOfRightElement].add(".") }
                        if (indexOfRightElement+1 < groups.size &&groups[indexOfRightElement+1].contains(".")) {
                            groups[indexOfRightElement+1] += groups[indexOfRightElement]
                            groups.removeAt(indexOfRightElement)
                        }
                        if (indexOfRightElement > 0 && groups[indexOfRightElement-1].contains(".")) {
                            groups[indexOfRightElement-1] += groups[indexOfRightElement]
                            groups.removeAt(indexOfRightElement)
                        }
                        groups[i] = group
                        if (g.size>group.size) {
                            val newList = ".".repeat(g.size-group.size).split("").filter { it.isNotEmpty() }.toMutableList()
                            groups.add(i+1, newList)
                        }
//                        groups = GetGroups(groups.reduce{acc, list -> (acc+list).toMutableList() })
                        return@replace
                    }
                }
            }
        }
        return groups.reduce{acc, list -> (acc+list).toMutableList() }
    }

    fun part1(input: String): BigInteger {
        val repr = GetDiskMapRepresentation(input)
        val compressed = ReorderDiskMapRepresentation(repr)
        var checksum = 0.toBigInteger()
        compressed.filterNot { it == "." }.forEachIndexed {index, character ->
            checksum += (index * character.toInt()).toBigInteger()
        }
        return checksum
    }

    fun part2(input: String): BigInteger {
        val repr = GetDiskMapRepresentation(input)
        val compressed = ReorderFiles(repr)
        var checksum = 0.toBigInteger()
        compressed.forEachIndexed {index, character ->
            if (character != ".") {
                checksum += (index * character.toInt()).toBigInteger()
            }
        }
        return checksum
    }

    val sampleInput = readInput("Day09_s")[0]
    val testInput = readInput("Day09")[0]
    part1(testInput).println()
    part2(testInput).println()
}