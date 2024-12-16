import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver
import kotlin.math.abs
import kotlin.math.round

fun main() {
    Loader.loadNativeLibraries()

    fun SolveMilp(aVector: Spot, bVector: Spot, target: Spot): Pair<Int, Int>? {
        /*
        * Having
        * x1: amount of A presses
        * x2: amount of B presses
        * Minimize
        * 3 * x1 + x2
        * Statement
        * aVector.x * x1 + bVector.x * x2 = target.x
        * aVector.y * x1 + bVector.y * x2 = target.y
        * Where
        * 0 <= x1 <= 100
        * 0 <= x2 <= 100
        * */


        val solver = MPSolver.createSolver("GLOP")
        val aPresses = solver.makeNumVar(0.0, 100.0, "aPresses")
        val bPresses = solver.makeNumVar(0.0, 100.0, "bPresses")
        val c0 = solver.makeConstraint(target.x.toDouble(), target.x.toDouble(), "c0")
        c0.setCoefficient(aPresses, aVector.x.toDouble())
        c0.setCoefficient(bPresses, bVector.x.toDouble())
        val c1 = solver.makeConstraint(target.y.toDouble(), target.y.toDouble(), "c1")
        c1.setCoefficient(aPresses, aVector.y.toDouble())
        c1.setCoefficient(bPresses, bVector.y.toDouble())
        val objective = solver.objective()
        objective.setCoefficient(aPresses, 3.0)
        objective.setCoefficient(bPresses, 1.0)
        objective.minimization()
        val resultStatus = solver.solve()
        return if (resultStatus == MPSolver.ResultStatus.OPTIMAL &&
            abs(aPresses.solutionValue() - round(aPresses.solutionValue())) < .001 &&
            abs(bPresses.solutionValue() - round(bPresses.solutionValue())) < .001
            ) {
            Pair(round(aPresses.solutionValue()).toInt(), round(bPresses.solutionValue()).toInt())
        } else {
            if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
                aPresses.solutionValue().println()
                bPresses.solutionValue().println()
            }
            null
        }

    }

    fun part1(input: List<String>): Int {
        val buttonRegex = """Button ([AB]): X\+(?<x>([0-9])+), Y\+(?<y>([0-9])+)""".toRegex()
        val targetRegex = """Prize: X=(?<x>([0-9])+), Y=(?<y>([0-9])+)""".toRegex()
        var total = 0
        input.chunked(4).forEachIndexed { idx, it ->
            var match = buttonRegex.find(it[0])!!
            val buttonA = Spot(match.groups["x"]!!.value.toInt(), match.groups["y"]!!.value.toInt())
            match = buttonRegex.find(it[1])!!
            val buttonB = Spot(match.groups["x"]!!.value.toInt(), match.groups["y"]!!.value.toInt())
            match = targetRegex.find(it[2])!!
            val prize = Spot(match.groups["x"]!!.value.toInt(), match.groups["y"]!!.value.toInt())
            val pair = SolveMilp(buttonA, buttonB, prize)
            if (pair != null) {
                total += (pair.first*3 + pair.second)
            } else {
                "Machine $idx has no resolution".println()
            }
        }
        return total
    }
    fun part2(input: String): Int {
        return 0
    }

    val sampleInput = readInput("Day13_s")
    val testInput = readInput("Day13")
    part1(testInput).println()
//    part2(testInput).println()
}