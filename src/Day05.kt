class Rule(input: String) {
    val before = input.split("|")[0].toInt()
    val after = input.split("|")[1].toInt()

    fun check(sequence: Sequence): Boolean {
        if ((before in sequence) && (after in sequence)){
            return sequence.indexOf(before) < sequence.indexOf(after)
        }
        return true
    }

    fun appliesTo(sequence: Sequence): Boolean {
        return (before in sequence) && (after in sequence)
    }
}

class Sequence(input: String) {
    private val sequence = input.split(",").map { it.toInt() }.toMutableList()

    operator fun contains(element: Int): Boolean {
        return sequence.contains(element)
    }

    fun indexOf(element: Int): Int {
        return sequence.indexOf(element)
    }

    fun getMiddleElement(): Int {
        return sequence[sequence.size/2]
    }

    fun reorderSequence(rules: List<Rule>) {
        val applicableRules = rules.filter { it.appliesTo(this) }
        // My magum opus: ridiculous silly sort
        while (!applicableRules.all{it.check(this)}) {
            applicableRules.forEach { rule ->
                if (!rule.check(this)) {
                    val store = indexOf(rule.after)
                    sequence[indexOf(rule.before)] = rule.after
                    sequence[store] = rule.before
                }
            }
        }
    }

    override fun toString(): String {
        return sequence.toString()
    }
}

fun main() {

    val sequences = mutableListOf<Sequence>()
    val rules = mutableListOf<Rule>()

    fun getRulesAndSequences(input: List<String>) {
        sequences.clear()
        rules.clear()
        input.forEach {
            if ("," in it) {
                sequences += Sequence(it)
            } else if ("|" in it){
                rules += Rule(it)
            }
        }
    }

    fun part1(input: List<String>): Int {
        getRulesAndSequences(input)
        var sum = 0
        sequences.forEach{ sequence ->
            if (rules.all { it.check(sequence) }) {
                println("Sequence $sequence is ok")
                sum += sequence.getMiddleElement()
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        getRulesAndSequences(input)
        var sum = 0
        sequences.forEach{ sequence ->
            if (!rules.all { it.check(sequence) }) {
                sequence.reorderSequence(rules)
                sum += sequence.getMiddleElement()
            }
        }

        return sum
    }

    val sampleInput = readInput("Day05_s")
    val testInput = readInput("Day05")

    part1(testInput).println()

    part2(testInput).println()
}
