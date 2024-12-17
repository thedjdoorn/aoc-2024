import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver
import kotlin.math.abs
import kotlin.math.round

fun main() {

    Loader.loadNativeLibraries()

    fun SolveMilp(aVector: Spot, bVector: Spot, target: ISpotLike, constrained: Boolean = false): Pair<Number, Number>? {
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

        val upperLimit = if (constrained) 100.0 else Double.POSITIVE_INFINITY
        val solver = MPSolver.createSolver("GLOP")
        val aPresses = solver.makeNumVar(0.0, upperLimit, "aPresses")
        val bPresses = solver.makeNumVar(0.0, upperLimit, "bPresses")
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
            Pair(round(aPresses.solutionValue()), round(bPresses.solutionValue()))
        } else {
            if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
                aPresses.solutionValue().println()
                bPresses.solutionValue().println()
            }
            null
        }

    }

    val buttonRegex = """Button ([AB]): X\+(?<x>([0-9])+), Y\+(?<y>([0-9])+)""".toRegex()
    val targetRegex = """Prize: X=(?<x>([0-9])+), Y=(?<y>([0-9])+)""".toRegex()

    fun part1(input: List<String>): Int {
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
                total += (pair.first.toInt()*3 + pair.second.toInt())
            } else {
//                "Machine $idx has no resolution".println()
            }
        }
        return total
    }
    fun part2(input: List<String>): Long {
        var total = 0L
        input.chunked(4).forEachIndexed { idx, it ->
            var match = buttonRegex.find(it[0])!!
            val buttonA = Spot(match.groups["x"]!!.value.toInt(), match.groups["y"]!!.value.toInt())
            match = buttonRegex.find(it[1])!!
            val buttonB = Spot(match.groups["x"]!!.value.toInt(), match.groups["y"]!!.value.toInt())
            match = targetRegex.find(it[2])!!
            val prize = LongSpot(match.groups["x"]!!.value.toInt()+10000000000000, match.groups["y"]!!.value.toInt()+10000000000000)
            val pair = SolveMilp(buttonA, buttonB, prize, false)
            if (pair != null) {
                total += (pair.first.toLong()*3 + pair.second.toLong())
            } else {
//                "Machine $idx has no resolution".println()
            }
        }
        return total
    }

    val sampleInput = readInput("Day13_s")
    val testInput = readInput("Day13")
    part1(sampleInput).println()
    part2(testInput).println()
}