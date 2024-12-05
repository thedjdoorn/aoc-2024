import java.lang.Integer.parseInt
import kotlin.math.abs
import kotlin.math.min

data class Coordinate(var x:Int, var y:Int)

fun Coordinate.move(x: Int, y: Int): Coordinate {
    return this.move(Coordinate(x, y))
}

fun Coordinate.move(coordinate: Coordinate): Coordinate {
    this.x += coordinate.x
    this.y += coordinate.y
    return this
}

class WordSearchGrid(input: List<String>) {
    val Grid = input.map {it.split("").filter { it.isNotEmpty() }}

    fun GetAtIndex(x: Int, y:Int): String {
        return if (x<0 || y< 0 || x >= Grid.size || y >= Grid[x].size) {
            " "
        } else {
            Grid[y][x]
        }
    }

    fun GetAtIndex(coordinate: Coordinate): String{
        return this.GetAtIndex(coordinate.x, coordinate.y)
    }

    fun GetIndicesOfString(input: String): List<Coordinate> {
        val result = mutableListOf<Coordinate>()
        Grid.forEachIndexed { index, row ->
            row.forEachIndexed { column, value ->
                if (value == input) {
                    result += Coordinate(column, index)
                }
            }
        }
        return result
    }

    fun GetSurroundingGrid(coordinate: Coordinate): WordSearchGrid?{
        if(coordinate.x == 0 || coordinate.y == 0 || coordinate.x == Grid[0].size-1 || coordinate.y == Grid.size-1){
            return null
        }
        coordinate.move(-1,-1)
        val rows = Grid.subList(coordinate.y, coordinate.y+3).map { it.subList(coordinate.x, coordinate.x+3).joinToString("") }

        return WordSearchGrid(rows);

    }

    fun FindPhrase(phrase: String, vectors: List<Coordinate>): Int {
        var result = 0
        val indices = GetIndicesOfString(phrase.substring(0, 1))
        indices.forEach { coord ->
            vectors.forEach{ vector ->
                val coordCopy = coord.copy()
                var foundString = phrase.substring(0,1)
                while (foundString.length < phrase.length) {
                    coordCopy.move(vector)
                    foundString += GetAtIndex(coordCopy)
                }
                if (foundString == phrase) {
                    result++
                }
            }
        }
        return result
    }

    override fun toString(): String {
        return Grid.map { line->line.joinToString("") }.joinToString("\n")
    }
}

fun main() {

    val TARGET_STRING = "XMAS"
    val vectors = listOf(
        Coordinate(0, 1),
        Coordinate(1, 0),
        Coordinate(1, 1),
        Coordinate(-1, 0),
        Coordinate(0, -1),
        Coordinate(-1, -1),
        Coordinate(-1, 1),
        Coordinate(1, -1),
    )

    val diagonalVectors = listOf(
        Coordinate(-1, -1),
        Coordinate(-1, 1),
        Coordinate(1, -1),
        Coordinate(1, 1)
    )

    fun part1(input: List<String>): Int {
        var result = 0
        val grid = WordSearchGrid(input)

        result = grid.FindPhrase(TARGET_STRING, vectors)
        result.println()
        result = 0
        val indices = grid.GetIndicesOfString("X")
        indices.forEach { coord ->
            vectors.forEach{ vector ->
                val c = coord.copy()
                var foundString = "X"
                while (foundString.length < 4){
                    c.move(vector)
                    foundString += grid.GetAtIndex(c)
                }
                if (foundString == TARGET_STRING) {
                    result++
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0;
        val grid = WordSearchGrid(input)
        val sgs = grid.GetIndicesOfString("A").mapNotNull { grid.GetSurroundingGrid(it) }
        sgs.forEach { subgrid ->
            var masFound = subgrid.FindPhrase("MAS", diagonalVectors)
            if (masFound >= 2) {
                result++
            }
        }

        return result
    }

    val sampleInput = readInput("Day04_s")
    val testInput = readInput("Day04")

//    part1(testInput).println()

    part2(testInput).println()
}
