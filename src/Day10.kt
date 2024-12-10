fun main() {

    fun Grid.GetIncrementingNeighbors(spot: Spot): List<Spot>{
        val spotValue = this.ValueAt(spot).toInt()
        if (spotValue == 9){
            return listOf()
        }
        val result = mutableListOf<Spot>()
        for(v in listOf(Spot(1,0), Spot(0,1), Spot(-1,0), Spot(0,-1))){
            var newSpot = spot.copy()
            newSpot = newSpot.moveBy(v)
            if (IsOnGrid(newSpot) && ValueAt(newSpot) != "" && ValueAt(newSpot).toInt() == spotValue+1) {
                result.add(newSpot)
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val zeroes = grid.GetSpotsWith("0")
        var result = 0
        zeroes.forEach { zero ->
            val nines = mutableListOf<Spot>()
            val neighbors = ArrayDeque(grid.GetIncrementingNeighbors(zero))
            while (neighbors.isNotEmpty()){
                val current = neighbors.removeFirst()
                if (grid.ValueAt(current).toInt() == 9 && current !in nines){
                    nines.add(current)
                } else {
                    grid.GetIncrementingNeighbors(current).forEach { neighbor ->
                        neighbors.add(neighbor)
                    }
                }
            }
            result += nines.size
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val zeroes = grid.GetSpotsWith("0")
        var result = 0
        zeroes.forEach { zero ->
            var nines = 0
            val neighbors = ArrayDeque(grid.GetIncrementingNeighbors(zero))
            while (neighbors.isNotEmpty()){
                val current = neighbors.removeFirst()
                if (grid.ValueAt(current).toInt() == 9){
                    nines++
                } else {
                    grid.GetIncrementingNeighbors(current).forEach { neighbor ->
                        neighbors.add(neighbor)
                    }
                }
            }
            result += nines
        }
        return result
    }

    val sampleInput = readInput("Day10_s")
    val testInput = readInput("Day10")
    part1(testInput).println()
    part2(testInput).println()
}