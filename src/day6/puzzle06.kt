package day6

import Puzzle
import PuzzleInput
import java.io.File

class Day6PuzzleInput(private val input: String, val numMarkers: Int, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    fun getPuzzleInput(): String {
        val puzzleInput = File(input).readText()
        check(puzzleInput.length >= numMarkers)
        return puzzleInput
    }
}
class Day6PuzzleSolutionMutableSet : Puzzle<Int, Day6PuzzleInput>() {
    override fun solution(input: Day6PuzzleInput): Int {
        val message = input.getPuzzleInput()
        val code = mutableSetOf<Char>()

        for (i in message.indices) {
            if (code.contains(message[i])) {
                val it = code.iterator()
                while (it.hasNext()) {
                    if (it.next() == message[i]) {
                        it.remove()
                        break
                    }
                    it.remove()
                }
            }
            code.add(message[i])
            if (code.size == input.numMarkers) {
                return i + 1
            }
        }

        error("Did not find start of stream")
    }
}
class Day6PuzzleSolutionArray : Puzzle<Int, Day6PuzzleInput>() {
    override fun solution(input: Day6PuzzleInput): Int {
        val message = input.getPuzzleInput()
        val code2 = CharArray(input.numMarkers)

        var end = 0
        var start = 0
        for (i in message.indices) {
            for(j in start until end) {
                if(message[i] == code2[j % input.numMarkers]) {
                    start = j + 1
                    break
                }
            }
            code2[end % input.numMarkers] = message[i]
            end++
            if(end - start == input.numMarkers) {
                return end
            }
        }

        error("Did not find start of stream")
    }
}

fun day6Puzzle() {
    val solutions = listOf(Day6PuzzleSolutionMutableSet(), Day6PuzzleSolutionArray())
    val inputs = listOf(
        Day6PuzzleInput("inputs/day6/example.txt", 4, 7),
        Day6PuzzleInput("inputs/day6/example2.txt", 4, 5),
        Day6PuzzleInput("inputs/day6/example3.txt", 4, 6),
        Day6PuzzleInput("inputs/day6/example4.txt", 4, 10),
        Day6PuzzleInput("inputs/day6/example5.txt", 4, 11),
        Day6PuzzleInput("inputs/day6/input.txt", 4, 1760),
        Day6PuzzleInput("inputs/day6/example.txt", 14, 19),
        Day6PuzzleInput("inputs/day6/example2.txt", 14, 23),
        Day6PuzzleInput("inputs/day6/example3.txt", 14, 23),
        Day6PuzzleInput("inputs/day6/example4.txt", 14, 29),
        Day6PuzzleInput("inputs/day6/example5.txt", 14, 26),
        Day6PuzzleInput("inputs/day6/input.txt", 14, 2974)
    )
    for(solution in solutions) {
        for(input in inputs) {
            solution.solve(input)
        }
    }
}


