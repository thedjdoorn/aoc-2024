fun main() {

    val robotRegex = "p=(?<px>([0-9]+)),(?<py>([0-9]+)) v=(?<vx>(-?[0-9]+)),(?<vy>(-?[0-9]+))".toRegex()

    fun getRobotsAfterIteration(input: List<String>, width: Int, height: Int, iteration: Int): List<Spot>{
        val robots = mutableListOf<Spot>()
        input.forEach {
            val match = robotRegex.find(it)!!.groups
            val start = Spot(match["px"]!!.value.toInt(), match["py"]!!.value.toInt())
            var vector = Spot(match["vx"]!!.value.toInt(), match["vy"]!!.value.toInt())
            vector *= iteration
            vector = Spot(vector.x%width, vector.y%height)
            var end = start.moveBy(vector)
            if (end.x < 0){
                end = end.moveBy(Spot(width,0))
            }
            if (end.x > width-1){
                end = end.moveBy(Spot(-width,0))
            }
            if (end.y < 0){
                end = end.moveBy(Spot(0,height))
            }
            if(end.y > height-1){
                end = end.moveBy(Spot(0,-height))
            }
            robots.add(end)
        }
        return robots
    }

    fun part1(input: List<String>, width: Int=101, height:Int=103): Int {
        val robots = getRobotsAfterIteration(input, width, height, 100)

        val groups = robots
            .filter { it.x != width/2 && it.y != height/2 }
            .groupBy { Pair(it.x < width/2, it.y < height/2) }
            .values.toMutableList()
        return groups.fold(1) {acc, list -> acc * list.size }
    }
    fun part2(input: List<String>): Int {
        val line = ".".repeat(101)
        val baseGrid = mutableListOf<String>()
        while (baseGrid.size<103){
            baseGrid.add(line)
        }
        for (i in 0..10000){
            val robots = getRobotsAfterIteration(input,101,103, i)
            // These vectors make a 3-high pyramid
            val vectors = mutableListOf(Spot(-1,1), Spot(0,1), Spot(1,1), Spot(-2,2), Spot(-1,2), Spot(0,2), Spot(1,2), Spot(2,2) )
            robots.forEach { robot->
                if (vectors.all { robot.moveBy(it) in robots }){
                    val grid = Grid(baseGrid)
                    robots.forEach{
                        grid.Mark(it)
                    }
                    grid.println()
                    return i // basically an educated guess
                }
            }
        }
        return 0
    }

    val sampleInput = readInput("Day14_s")
    val testInput = readInput("Day14")
//    part1(sampleInput, 11,7).println()
    part1(testInput).println()
    part2(testInput).println()
}