package day9

import Puzzle
import PuzzleInput
import java.io.File
import kotlin.math.abs

fun day9Puzzle() {

    Day9PuzzleSolution().solve(Day9PuzzleInput("inputs/day9/example.txt", 2, 13))
    Day9PuzzleSolution().solve(Day9PuzzleInput("inputs/day9/input.txt", 2, 6271))
    Day9PuzzleSolution().solve(Day9PuzzleInput("inputs/day9/example.txt", 10, 1))
    Day9PuzzleSolution().solve(Day9PuzzleInput("inputs/day9/example2.txt", 10, 36))
    Day9PuzzleSolution().solve(Day9PuzzleInput("inputs/day9/input.txt", 10, 2458))
}

class Day9PuzzleInput(val input: String, numKnots: Int, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    val moves = File(input).readLines()
    val pointsVisited = HashMap<Pair<Int, Int>, Boolean>()
    private val knots = Array(numKnots) {
        Pair(0, 0)
    }

    private fun getKnot(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
        val x = head.first - tail.first
        val y = head.second - tail.second
        if(abs(x) > 1 || abs(y) > 1) {
            return Pair(tail.first + x.coerceIn(-1, 1), tail.second + y.coerceIn(-1, 1))
        }
        return tail
    }

    private fun moveHead(newHead: Pair<Int, Int>) {
        knots[0] = newHead
        for(i in 1 until knots.size) {
            knots[i] = getKnot(knots[i-1], knots[i])
        }
        pointsVisited[knots.last()] = true
    }
    fun moveHead(xOffset: Int, yOffset: Int, amount: Int) {
        for(move in 1 .. amount) {
            moveHead(Pair(knots[0].first + xOffset, knots[0].second + yOffset))
        }
    }
}

class Day9PuzzleSolution : Puzzle<Int, Day9PuzzleInput>() {
    override fun solution(input: Day9PuzzleInput): Int {
        input.pointsVisited[Pair(0, 0)] = true
        input.moves.forEach { move ->
            val moveParts = move.split(" ")
            val direction = moveParts[0]
            val amount = moveParts[1].toInt()
            when(direction) {
                "U" -> input.moveHead(0, 1, amount)
                "D" -> input.moveHead(0, -1, amount)
                "L" -> input.moveHead(-1, 0, amount)
                "R" -> input.moveHead(1, 0, amount)
            }
        }

        return input.pointsVisited.size
    }

}