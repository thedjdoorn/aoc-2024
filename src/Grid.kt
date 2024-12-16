class Grid(input: List<String>){
    val grid = input.map { it.split("").filter { it.isNotBlank() }.toMutableList() }.toMutableList()

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

    fun IsNeighbor(s: Spot): Boolean{
        Direction.entries.forEach{
            if (this.GetRelativeSpot(it) == s) return true
        }
        return false
    }
}
