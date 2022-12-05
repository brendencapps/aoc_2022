import java.io.File
import java.lang.Integer.min
import java.util.*
import kotlin.math.max

fun main() {
    check(day5Puzzle1("day5_example.txt", getExampleStart()) == "CMZ")
    println(day5Puzzle1("day5_input.txt", getInputStart())) // TWSGQHNHL
    check(day5Puzzle2("day5_example.txt", getExampleStart()) == "MCD")
    println(day5Puzzle2("day5_input.txt", getInputStart())) // JNRSCDWPP
}

fun getInputStart(): List<Vector<Char>> {
    return listOf("LNWTD", "CPH", "WPHNDGMJ", "CWSNTQL", "PHCN", "THNDMWQB", "MBRJGSL", "ZNWGVBRT", "WGDNPL").map {Vector(it.toList()) }
}
fun getExampleStart(): List<Vector<Char>> {
    return mutableListOf("ZN", "MCD", "P").map{ Vector(it.toList()) }
}

fun day5Puzzle1(input: String, stacks: List<Vector<Char>>): String {
    return day5Puzzle(input, stacks) { currentStacks, crates, from, to ->
        for(i in 1 ..crates) {
            if(currentStacks[from].isNotEmpty()) {
                currentStacks[to].add(currentStacks[from].removeLast())
            }
        }
    }
}

fun day5Puzzle2(input: String, stacks: List<Vector<Char>>): String {
    return day5Puzzle(input, stacks) { currentStacks, crates, from, to ->
        val start = max(0, currentStacks[from].size - crates)
        val end = min(currentStacks[from].size - 1, start + crates)
        for(i in start .. end) {
            currentStacks[to].add(currentStacks[from].removeAt(start))
        }
    }
}

fun day5Puzzle(input: String, stacks: List<Vector<Char>>, moveOp: (stacks: List<Vector<Char>>, crates: Int, from: Int, to: Int) -> Unit): String {
    val inputParser = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
    File(input).readLines().forEach { action ->
        val (crates, from, to) = inputParser.find(action)!!.destructured
        moveOp(stacks, crates.toInt(), from.toInt() - 1, to.toInt() - 1)
    }

    return stacks.map { stack ->
        if(stack.isNotEmpty()) {
            stack.last()
        }
        else {
            " "
        }
    }.joinToString("")
}

