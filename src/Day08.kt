
fun Grid.forEach(f: (String)->Unit){
    this.grid.forEach{ row ->
        row.forEach(f)
    }
}

fun Grid.forEachIndexed(f: (Int, Int, String)->Unit){
    this.grid.forEachIndexed{ yIndex,row ->
        row.forEachIndexed{xIndex,item ->
            f(xIndex, yIndex, item)
        }
    }
}

// Returns the delta between two spots
fun Spot.VectorTo(spot: Spot): Spot{
    return Spot(spot.x-this.x, spot.y-this.y)
}

// Particularly useful to flip vectors
fun Spot.Negate() : Spot {
    return Spot(-this.x, -this.y)
}

operator fun Spot.times(int: Int): Spot{
    return Spot(this.x * int, this.y * int)
}

fun Spot.moveBy(spot: Spot): Spot{
    return Spot(spot.x+this.x, spot.y+this.y)
}

fun main() {

    fun getSpotLists(grid:Grid): MutableMap<String, MutableList<Spot>> {
        val spotLists = mutableMapOf<String, MutableList<Spot>>()
        grid.forEachIndexed{x,y,item ->
            if (item !in listOf(".", "")){
                if (!spotLists.containsKey(item)) {
                    spotLists[item] = mutableListOf()
                }
                spotLists[item]!!.add(Spot(x,y))
            }
        }
        return spotLists
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val spotLists = getSpotLists(grid)
        spotLists.forEach { key, list ->
            list.forEach { spot ->
                list.forEach{ otherSpot ->
                    if (spot == otherSpot) return@forEach
                    if (grid.IsOnGrid(otherSpot.moveBy(spot.VectorTo(otherSpot)))){
                        grid.Mark(otherSpot.moveBy(spot.VectorTo(otherSpot)))
                    }
                    if (grid.IsOnGrid(spot.moveBy(spot.VectorTo(otherSpot).Negate()))){
                        grid.Mark(spot.moveBy(spot.VectorTo(otherSpot).Negate()))
                    }
                }
            }
        }
        var result = 0
        grid.forEach { if (it == "X") result++ }
        grid.println()
        return result
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val spotLists = getSpotLists(grid)
        spotLists.forEach { key, list ->
            list.forEach { spot ->
                grid.Mark(spot)
                list.forEach{ otherSpot ->
                    if (spot == otherSpot) return@forEach
                    var vector = spot.VectorTo(otherSpot)
                    var newAntinode = otherSpot.moveBy(vector)
                    while (grid.IsOnGrid(newAntinode)){
                        grid.Mark(newAntinode)
                        newAntinode = newAntinode.moveBy(vector)
                    }
                    vector = vector.Negate()
                    newAntinode = spot.moveBy(vector)
                    while (grid.IsOnGrid(newAntinode)){
                        grid.Mark(newAntinode)
                        newAntinode = newAntinode.moveBy(vector)
                    }
                }
            }
        }
        var result = 0
        grid.forEach { if (it == "X") result++ }
        grid.println()
        return result
    }

    val sampleInput = readInput("Day08_s")
    val testInput = readInput("Day08")
    part1(testInput).println()
    part2(testInput).println()
}