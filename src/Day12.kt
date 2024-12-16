fun main() {

    fun SamePlot(grid:Grid, spot1:Spot, spot2:Spot): Boolean {
        return grid.ValueAt(spot1) == grid.ValueAt(spot2) && spot1.IsNeighbor(spot2)
    }

    fun Spot.GetSameNeighbors(grid: Grid): List<Spot>{
        val value = grid.ValueAt(this)
        val result = mutableListOf<Spot>()
        Direction.entries.forEach{
            val newSpot = this.GetRelativeSpot(it)
            if (grid.IsOnGrid(newSpot) && grid.ValueAt(newSpot) == value){
                result.add(newSpot)
            }
        }
        return result
    }

    fun GetGroups(grid: Grid): MutableList<MutableList<Spot>> {
        val groups = mutableListOf<MutableList<Spot>>()
        grid.grid.forEachIndexed { yIndex, row ->
            row.forEachIndexed itemLoop@ { xIndex, value->
                print("Current spot: $xIndex, $yIndex \r")
                if (groups.isNotEmpty() && Spot(xIndex, yIndex) in groups.reduce{ acc, it -> (acc+it).toMutableList() }){
                    return@itemLoop
                }
                val group = mutableListOf(Spot(xIndex, yIndex))
                val deque = ArrayDeque(listOf(Spot(xIndex, yIndex)))
                while (!deque.isEmpty()){
                    val spot = deque.removeFirst()
                    spot.GetSameNeighbors(grid).forEach {
                        if (it !in group){
                            deque.add(it)
                            group.add(it)
                        }
                    }
                }
                groups.add(group)
            }
        }
        return groups
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val groups = GetGroups(grid)
        var result = 0
        groups.forEach { group ->
            val spotValue = grid.ValueAt(group[0])
            val area = group.size
            var perimeter = 0
            group.forEach{spot->
                Direction.entries.forEach{
                    val relativeSpot = spot.GetRelativeSpot(it)
                    if (!grid.IsOnGrid(relativeSpot) || grid.ValueAt(relativeSpot) != spotValue) {
                        perimeter += 1
                    }
                }
            }
            result += perimeter * area
        }
        return result
    }
    fun part2(input: String): Int {
        return 0
    }

    val sampleInput = readInput("Day12_s2")
    val testInput = readInput("Day12")
    part1(testInput).println()
//    part2(testInput).println()
}