package day12

import Puzzle
import PuzzleInput
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import java.io.File
import kotlin.coroutines.coroutineContext
import kotlin.math.abs

fun day12Puzzle() {
    //Day12PuzzleSolution().solve(Day12PuzzleInput("inputs/day12/example.txt", 31))
    //Day12PuzzleSolution().solve(Day12PuzzleInput("inputs/day12/input.txt"))
    Day12Puzzle2Solution().solve(Day12PuzzleInput("inputs/day12/example.txt", 1, 29))
    Day12Puzzle2Solution().solve(Day12PuzzleInput("inputs/day12/input.txt", 1))
}

data class Location <T> (val row: Int, val col: Int, val map: List<List<T>>) {
    fun left() : Location<T> {
        return Location(row, col - 1, map)
    }
    fun right() : Location<T> {
        return Location(row, col + 1, map)
    }
    fun top() : Location<T> {
        return Location(row - 1, col, map)
    }
    fun bottom() : Location<T> {
        return Location(row + 1, col, map)
    }

    fun getNeighbors(): List<Location<T>> {
        return listOf(left(), top(), right(), bottom())
    }

    fun inMap(): Boolean {
        return if(row < 0 || row >= map.size) false
        else col >= 0 && col < map[row].size
    }

    fun mapValue() : T {
        return map[row][col]
    }
}

data class Move (val original: Location<Int>, val next: Location<Int>) {
    fun valid(): Boolean {
        return next.inMap() && (next.mapValue() - original.mapValue() <= 1)
    }
}

class Day12PuzzleInput(val input: String, sMap: Int = 0, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    val terrain: List<List<Int>> = File(input).readLines().map { line ->
        line.toCharArray().map { char ->
            if (char == 'S') sMap
            else if (char == 'E') 27
            else char - 'a' + 1
        }
    }
}

class Day12PuzzleSolution : Puzzle<Int, Day12PuzzleInput>() {

    override fun solution(input: Day12PuzzleInput): Int {
        return Solver().getSolution(findStart(input))
    }

    private fun findStart(input: Day12PuzzleInput): Location<Int> {
        for(row in input.terrain.indices) {
            for(col in input.terrain[row].indices) {
                if(input.terrain[row][col] == 0) {
                    return Location(row, col, input.terrain)
                }
            }
        }
        error("Did not find starting position")
    }
}

class Solver {

    private var paths = mutableListOf<MutableList<Location<Int>>>()
    private var solution = Int.MAX_VALUE
    private val moves = mutableListOf<Move>()
    private var highestPoint = 0

    fun getSolution(start: Location<Int>) : Int {
        paths.add(mutableListOf(start))
        while(paths.isNotEmpty()) {
            val newPaths = mutableListOf<MutableList<Location<Int>>>()
            paths.forEach { path ->
                val current = path.last()
                val validMoves = current.getNeighbors().filter { nextLocation ->
                    val move = Move(current, nextLocation)
                    move.valid() && !moves.contains(move) && !path.contains(nextLocation)
                }
                validMoves.forEach { move ->
                    val newPath = path.toCollection(mutableListOf())
                    moves.add(Move(current, move))
                    newPath.add(move)
                    if(move.mapValue() > highestPoint) {
                        highestPoint = move.mapValue()
                    }
                    if(move.mapValue() == 27) {
                        if(newPath.size < solution) {
                            solution = newPath.size - 1
                        }
                    }
                    else {
                        newPaths.add(newPath)
                    }
                }
            }
            paths = newPaths.filter { path ->
                path.size < solution
            }.toMutableList()

        }
        return solution
    }
}

class Day12Puzzle2Solution : Puzzle<Int, Day12PuzzleInput>() {
    override fun solution(input: Day12PuzzleInput): Int {
        return runBlocking {
            val locations = mutableListOf<Location<Int>>()
            input.terrain.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, height ->
                    if (height <= 1) {
                        locations.add(Location(rowIndex, colIndex, input.terrain))
                    }
                }
            }
            locations.map { location ->
                async {
                    val solution = Solver().getSolution(location)
                    println("$solution ${location.row} ${location.col}")
                    solution
                }
            }.awaitAll().min()

        }
    }

}