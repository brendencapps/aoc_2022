package day1

import Puzzle
import PuzzleInput
import java.io.File
import java.util.PriorityQueue

fun day1Puzzle() {
    Day1PuzzleSolution1(1).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 24000))
    Day1PuzzleSolution1(2).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 35000))
    Day1PuzzleSolution1(3).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 45000))
    Day1PuzzleSolution1(4).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 51000))
    Day1PuzzleSolution1(5).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 55000))
    Day1PuzzleSolution1(1).solve(Day1PuzzleInput("inputs/day1/caloriesInput.txt", 66487))

    Day1PuzzleSolution2(1).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 24000))
    Day1PuzzleSolution2(2).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 35000))
    Day1PuzzleSolution2(3).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 45000))
    Day1PuzzleSolution2(4).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 51000))
    Day1PuzzleSolution2(5).solve(Day1PuzzleInput("inputs/day1/exampleData.txt", 55000))
    Day1PuzzleSolution2(3).solve(Day1PuzzleInput("inputs/day1/caloriesInput.txt", 197301))
}

class Day1PuzzleInput(private val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {

    fun sumTop(op: (List<Int>) -> Int): Int {
        val calorieList = File(input)
            .readText()
            .split("\r\n\r\n")
            .map { elf -> elf.lines().sumOf { calories -> calories.toInt() } }
        return op(calorieList)
    }
}

class Day1PuzzleSolution1(private val numElves: Int) : Puzzle<Int, Day1PuzzleInput>() {
    override fun solution(input: Day1PuzzleInput): Int {
        return input.sumTop { calorieList ->
            val topCalories = PriorityQueue<Int>()
            for(calories in calorieList) {
                topCalories.add(calories)
                if(topCalories.size > numElves) { topCalories.poll() }
            }
            topCalories.sum()
        }
    }
}

class Day1PuzzleSolution2(private val numElves: Int) : Puzzle<Int, Day1PuzzleInput>() {
    override fun solution(input: Day1PuzzleInput): Int {
        return input.sumTop { calorieList ->
            calorieList.sortedDescending().take(numElves).sum()
        }
    }
}

