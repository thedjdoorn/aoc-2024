import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

enum class Direction(val x: Int, val y: Int) {
    UP(0,-1),
    RIGHT(1,0),
    DOWN(0,1),
    LEFT(-1,0)
}

fun Direction.Next(): Direction {
    return when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
    }
}

data class Spot(val x: Int, val y: Int){
    fun GetRelativeSpot(d: Direction): Spot{
        return Spot(x + d.x, y + d.y)
    }
}

data class Bump(val spot: Spot, val direction: Direction){
    override fun equals(other: Any?): Boolean {
        return (other as Bump).spot.x == spot.x && other.spot.y == spot.y && other.direction == direction
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

class Grid(input: List<String>){
    val grid = input.map { it.split("").toMutableList() }.toMutableList()

    fun IsOnGrid(x: Int, y: Int): Boolean{
        return (y in 0..(grid.size - 1)) && (x in 0..(grid[0].size - 1))
    }

    fun IsOnGrid(spot:Spot): Boolean{
        return IsOnGrid(spot.x, spot.y)
    }

    fun IsOccupied(x: Int, y:Int): Boolean {
        return IsOnGrid(x,y) && grid[y][x] in listOf("#", "O")
    }

    // Checks whether the next spot is occupied
    fun IsOccupied(spot: Spot, direction: Direction): Boolean {
        val (x,y) = spot.GetRelativeSpot(direction)
        return IsOccupied(x,y)
    }

    fun Mark(s:Spot) {
//      Dirty hack to prevent the marking of invalid positions
        if (grid[s.y][s.x] !in listOf("", "\r", "\n")){
            grid[s.y][s.x] = "X"
        }
    }

    fun FindStart(): Spot? {
        grid.forEachIndexed{y,row ->
            row.forEachIndexed{x,item ->
                if (item == "^"){
                    return Spot(x,y)
                }
            }
        }
        return null
    }

    fun CountSteps(): Int {
        var sum = 0
        grid.forEach{row -> sum += row.filter{it=="X"}.size }
        return sum
    }

    override fun toString(): String {
        return grid.map{it.joinToString("")}.joinToString("\n")
    }

    fun AddObstacle(spot: Spot){
        grid[spot.y][spot.x] = "O"
    }

    fun Loops(): Boolean {
        val bumps = mutableListOf<Bump>()
        var currentSpot = this.FindStart()!!
        var currentDirection = Direction.UP
        while (IsOnGrid(currentSpot.x, currentSpot.y) ) {
            var offGrid = false
            while (!IsOccupied(currentSpot, currentDirection) && !offGrid) {
                if (IsOnGrid(currentSpot)){
                    currentSpot = currentSpot.GetRelativeSpot(currentDirection)
                } else {
                    offGrid = true
                }
            }
            val newBump = Bump(currentSpot, currentDirection)
            if (newBump in bumps) {
                return true
            }
            bumps.add(newBump)
            currentDirection = currentDirection.Next()
        }
        return false
    }

    fun ValueAt(spot: Spot): String {
        return grid[spot.y][spot.x]
    }

    fun GetSpotsWith(find: String): List<Spot>{
        val result = mutableListOf<Spot>()
        grid.forEachIndexed{yIndex, row ->
            row.forEachIndexed{xIndex,value ->
                if (find == value){
                    result.add(Spot(xIndex, yIndex))
                }
            }
        }
        return result
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        var currentSpot = grid.FindStart()!!
        var currentDirection = Direction.UP
        while (grid.IsOnGrid(currentSpot.x, currentSpot.y) ) {
            currentSpot.println()
            var offGrid = false
            while (!grid.IsOccupied(currentSpot, currentDirection) && !offGrid) {
                if (grid.IsOnGrid(currentSpot)){
                    grid.Mark(currentSpot)
                    currentSpot = currentSpot.GetRelativeSpot(currentDirection)
                } else {
                    offGrid = true
                }
            }
            currentDirection = currentDirection.Next()
        }
        grid.println()
        return grid.CountSteps()
    }

    fun part2(input: List<String>): Int {
        var result = 0
        for (y in 0..input.size-1){
            runBlocking {
                withContext(Dispatchers.Default) {
                    for (x in 0..input[0].length - 1) {
                        launch {
                            val copy = Grid(input)
                            if (copy.grid[y][x] != "^") {
                                copy.AddObstacle(Spot(x, y))
                                if (copy.Loops()) {
                                    result++
                                }

                            }
                        }
                    }
                }
            }
        }
        return result
    }

    val sampleInput = readInput("Day06_s")
    val testInput = readInput("Day06")
    part1(testInput).println()

    val time = measureTimeMillis {
        part2(testInput).println()
    }
    "Finished part 2 in $time ms".println()
}
