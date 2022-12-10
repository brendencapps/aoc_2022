package day10

import Puzzle
import PuzzleInput
import java.io.File

fun day10Puzzle() {
    Day10PuzzleSolution().solve(Day10PuzzleInput("inputs/day10/example.txt", 13140))
    Day10PuzzleSolution().solve(Day10PuzzleInput("inputs/day10/input.txt", 13860))
    //Day10Puzzle2Solution().solve(Day10PuzzleInput("inputs/day10/example.txt", 0))
    Day10Puzzle2Solution().solve(Day10PuzzleInput("inputs/day10/input.txt", 0))
}

class Day10PuzzleInput(val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult)

class Day10PuzzleSolution : Puzzle<Int, Day10PuzzleInput>() {
    private val cyclesToMeasure = setOf(20, 60, 100, 140, 180, 220)
    private var cycle = 0
    var x = 1
    override fun solution(input: Day10PuzzleInput): Int {
        return File(input.input).readLines().sumOf {operation ->
            var score = 0
            if(operation.startsWith("noop")) {
                score += nextCycle()
            }
            else {
                score += nextCycle() + nextCycle()
                x += operation.split(" ")[1].toInt()
            }
            if(score != 0) println("$cycle $score")
            score
        }
    }

    private fun nextCycle(): Int {
        cycle++
        return if(cyclesToMeasure.contains(cycle)) x * cycle else 0
    }
}

class Day10Puzzle2Solution : Puzzle<Int, Day10PuzzleInput>() {
    private var cycle = 0
    private var x = 1
    override fun solution(input: Day10PuzzleInput): Int {
        File(input.input).readLines().forEach { operation ->
            if(operation.startsWith("noop")) {
                runCycle()
            }
            else {
                runCycle()
                runCycle()
                x += operation.split(" ")[1].toInt()
            }
        }
        return 0
    }
    private fun runCycle() {
        if(cycle != 0 && cycle % 40 == 0) println()
        if(kotlin.math.abs(x - cycle % 40) <= 1) {
            print("#")
        }
        else {
            print(" ")
        }
        cycle++
    }

}