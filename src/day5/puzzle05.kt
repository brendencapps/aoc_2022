import java.io.File
import java.lang.Integer.min
import java.util.*
import kotlin.math.max

data class Move(val crate: Int, val from: Int, val to: Int)

class Day5PuzzleInput(val stacks: List<Vector<Char>>, private val moves: String, expectedResult: String? = null) : PuzzleInput<String>(expectedResult) {
    fun doMoves(op: (stacks: List<Vector<Char>>, move: Move) -> Unit) {
        val inputParser = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
        File(moves).readLines().forEach { action ->
            val (crates, from, to) = inputParser.find(action)!!.destructured
            op(stacks, Move(crates.toInt(), from.toInt() - 1, to.toInt() - 1))
        }
    }
}
class Day5PuzzleSolutionPuzzle1 : Puzzle<String, Day5PuzzleInput>() {
    override fun solution(input: Day5PuzzleInput): String {
        input.doMoves { stacks, move ->
            for(i in 1 ..move.crate) {
                if(stacks[move.from].isNotEmpty()) {
                    stacks[move.to].add(stacks[move.from].removeLast())
                }
            }
        }
        return input.stacks.map { stack ->
            if(stack.isNotEmpty()) {
                stack.last()
            }
            else {
                " "
            }
        }.joinToString("")
    }
}
class Day5PuzzleSolutionPuzzle2 : Puzzle<String, Day5PuzzleInput>() {
    override fun solution(input: Day5PuzzleInput): String {
        input.doMoves { stacks, move ->
            val start = max(0, stacks[move.from].size - move.crate)
            val end = min(stacks[move.from].size - 1, start + move.crate)
            for(i in start .. end) {
                stacks[move.to].add(stacks[move.from].removeAt(start))
            }
        }
        return input.stacks.map { stack ->
            if(stack.isNotEmpty()) {
                stack.last()
            }
            else {
                " "
            }
        }.joinToString("")
    }
}
fun day5Puzzle() {

    val inputDir = "inputs/day5"

    Day5PuzzleSolutionPuzzle1().solve(Day5PuzzleInput(getExampleStart(), "$inputDir/example.txt", "CMZ"))
    Day5PuzzleSolutionPuzzle1().solve(Day5PuzzleInput(getInputStart(), "$inputDir/input.txt", "TWSGQHNHL"))
    Day5PuzzleSolutionPuzzle2().solve(Day5PuzzleInput(getExampleStart(), "$inputDir/example.txt", "MCD"))
    Day5PuzzleSolutionPuzzle2().solve(Day5PuzzleInput(getInputStart(), "$inputDir/input.txt", "JNRSCDWPP"))

}

fun getInputStart(): List<Vector<Char>> {
    return listOf("LNWTD", "CPH", "WPHNDGMJ", "CWSNTQL", "PHCN", "THNDMWQB", "MBRJGSL", "ZNWGVBRT", "WGDNPL").map {Vector(it.toList()) }
}
fun getExampleStart(): List<Vector<Char>> {
    return mutableListOf("ZN", "MCD", "P").map{ Vector(it.toList()) }
}

