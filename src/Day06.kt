import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

data class Bump(val spot: Spot, val direction: Direction){
    override fun equals(other: Any?): Boolean {
        return (other as Bump).spot.x == spot.x && other.spot.y == spot.y && other.direction == direction
    }

    override fun hashCode(): Int {
        return super.hashCode()
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
