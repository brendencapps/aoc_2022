package day3

import Puzzle
import PuzzleInput
import java.io.File

fun day3Puzzle() {
    val inputDir = "inputs/day3"
    Day3Puzzle1Solution().solve(Day3PuzzleInput("$inputDir/example.txt", 157))
    Day3Puzzle1Solution().solve(Day3PuzzleInput("$inputDir/input.txt", 8039))
    Day3Puzzle2Solution().solve(Day3PuzzleInput("$inputDir/example.txt", 70))
    Day3Puzzle2Solution().solve(Day3PuzzleInput("$inputDir/input.txt", 2510))
}

class Day3PuzzleInput(private val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {

    fun sum(op: (String) -> Int): Int {
        return File(input).readLines().sumOf { rucksack -> op(rucksack) }
    }

    fun sum(chunk: Int, op: (List<String>) -> Int): Int {
        return File(input).readLines().chunked(chunk).sumOf { rucksack -> op(rucksack) }
    }
}

class Day3Puzzle1Solution : Puzzle<Int, Day3PuzzleInput>() {
    override fun solution(input: Day3PuzzleInput): Int {
        return input.sum { rucksack ->
            val common = rucksack.substring(0 until rucksack.length / 2)
                .toSet()
                .intersect(rucksack.substring(rucksack.length / 2).toSet())
            check(common.size == 1)
            common.first().priority()
        }
    }
}

class Day3Puzzle2Solution : Puzzle<Int, Day3PuzzleInput>() {
    override fun solution(input: Day3PuzzleInput): Int {
        return input.sum(3) { rucksacks ->
            var set = rucksacks[0].toSet()
            for(i in 1 until rucksacks.size) {
                set = set.intersect(rucksacks[i].toSet())
            }
            check(set.size == 1)
            set.first().priority()
        }
    }
}

fun Char.priority(): Int {
    check(isLetter())
    return if(isLowerCase()) {
        this - 'a' + 1
    }
    else {
        this - 'A' + 27
    }
}
