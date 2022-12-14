package day14

import Puzzle
import PuzzleInput
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min


fun day14Puzzle() {
    Day14PuzzleSolution().solve(Day14PuzzleInput("inputs/day14/example.txt", 24))
    Day14PuzzleSolution().solve(Day14PuzzleInput("inputs/day14/input.txt", 1016))
    Day14Puzzle2Solution().solve(Day14PuzzleInput("inputs/day14/example.txt", 93))
    Day14Puzzle2Solution().solve(Day14PuzzleInput("inputs/day14/input.txt", 25402))
}

private const val maxWidth = 400
private const val maxHeight = 200
open class Day14PuzzleInput(val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    private val grid: Array<IntArray> = Array(maxHeight) { IntArray(maxWidth) { 0 } }
    val floor: Int
    init {
        floor = File(input).readLines().map { rockPath ->
            val points = rockPath.split(" -> ").map{rockPoint ->
                val coordinates = rockPoint.split(",")
                Pair(coordinates[1].toInt(), coordinates[0].toInt() - 500 + maxWidth / 2)
            }
            for(i in 0 until points.size - 1) {
                setRockFaces(points[i], points[i+1])
            }
            points
        }.flatten().maxOf { it.first } + 2
    }

    fun getGridValue(point: Pair<Int, Int>) : Int {
        return grid[point.first][point.second]
    }

    fun setGridValue(point: Pair<Int, Int>, value: Int) {
        grid[point.first][point.second] = value
    }

    private fun setRockFaces(point1: Pair<Int, Int>, point2: Pair<Int, Int>) {
        if(point1.first == point2.first) {
            val start = min(point1.second, point2.second)
            val finish = max(point1.second, point2.second)
            for(i in start .. finish) {
                grid[point1.first][i] = 1
            }
        }
        else if(point1.second == point2.second) {
            val start = min(point1.first, point2.first)
            val finish = max(point1.first, point2.first)
            for(i in start .. finish) {
                grid[i][point1.second] = 1
            }
        }
    }
}


class Day14PuzzleSolution : Puzzle<Int, Day14PuzzleInput>() {

    override fun solution(input: Day14PuzzleInput): Int {
        var sand = 0
        addAbyssToGrid(input)
        while(true) {
            var sandPoint = Pair(0, maxWidth / 2)
            while(true) {
                val next = listOf(
                    Pair(sandPoint.first + 1, sandPoint.second),
                    Pair(sandPoint.first + 1, sandPoint.second - 1),
                    Pair(sandPoint.first + 1, sandPoint.second + 1)).filter {input.getGridValue(it) <= 0}
                if(next.isEmpty()) {
                    sand++
                    input.setGridValue(sandPoint, 2)
                    break
                }
                when(input.getGridValue(next.first())) {
                    -1 -> return sand
                    0 -> sandPoint = next.first()
                }
            }
        }
    }

    private fun addAbyssToGrid(input: Day14PuzzleInput) {
        for(col in 0 until maxWidth) {
            for(row in maxHeight - 1 downTo 0) {
                if(input.getGridValue(Pair(row, col)) == 0) {
                    input.setGridValue(Pair(row, col), -1)
                }
                else if(input.getGridValue(Pair(row, col)) == 1) {
                    break
                }
            }
        }
    }
}

class Day14Puzzle2Solution : Puzzle<Int, Day14PuzzleInput>() {
    override fun solution(input: Day14PuzzleInput): Int {
        var sand = 0
        setGridFloor(input)
        while(true) {
            var sandPoint = Pair(0, 200)
            while(true) {
                val next = listOf(
                    Pair(sandPoint.first + 1, sandPoint.second),
                    Pair(sandPoint.first + 1, sandPoint.second - 1),
                    Pair(sandPoint.first + 1, sandPoint.second + 1)).filter {input.getGridValue(it) <= 0}
                if(next.isEmpty()) {
                    input.setGridValue(sandPoint, 2)
                    sand++
                    if(sandPoint.first == 0) {
                        return sand
                    }
                    break
                }
                sandPoint = next.first()
            }
        }
    }
    private fun setGridFloor(input: Day14PuzzleInput) {

        for(col in 0 until 400) {
            for(row in 199 downTo input.floor) {
                input.setGridValue(Pair(row, col),1)
            }
        }
    }

}