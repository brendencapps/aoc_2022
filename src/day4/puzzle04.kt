package day4

import Puzzle
import PuzzleInput
import java.io.File

fun day4Puzzle() {
    val inputDir = "inputs/day4"
    Day4Puzzle1Solution().solve(Day4PuzzleInput("$inputDir/example.txt", 2))
    Day4Puzzle1Solution().solve(Day4PuzzleInput("$inputDir/input.txt", 305))
    Day4Puzzle2Solution().solve(Day4PuzzleInput("$inputDir/example.txt", 4))
    Day4Puzzle2Solution().solve(Day4PuzzleInput("$inputDir/input.txt", 811))
}

data class ElfSectionPair(val input: String) {
    private val elf1Section: IntRange
    private val elf2Section: IntRange

    init {
        val inputParser = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()
        val (start1, end1, start2, end2) = inputParser.find(input)!!.destructured
        elf1Section = start1.toInt() .. end1.toInt()
        elf2Section = start2.toInt() .. end2.toInt()
    }

    fun duplicateWork() : Boolean {
        return elf1Section.fullyContains(elf2Section) || elf2Section.fullyContains(elf1Section)
    }

    fun overlaps() : Boolean {
        return elf1Section.overlaps(elf2Section)
    }
}

class Day4PuzzleInput(private val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {

    fun count(op: (ElfSectionPair) -> Boolean): Int {
        return File(input).readLines().count { elfPair ->
            op(ElfSectionPair(elfPair))
        }
    }
}
class Day4Puzzle1Solution : Puzzle<Int, Day4PuzzleInput>() {
    override fun solution(input: Day4PuzzleInput): Int {
        return input.count {elfSelectionPair ->
            elfSelectionPair.duplicateWork()
        }
    }
}

class Day4Puzzle2Solution : Puzzle<Int, Day4PuzzleInput>() {
    override fun solution(input: Day4PuzzleInput): Int {
        return input.count {elfSelectionPair ->
            elfSelectionPair.overlaps()
        }
    }
}

fun IntRange.fullyContains(other: IntRange): Boolean {
    return first >= other.first && last <= other.last
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return other.first in this || other.last in this || first in other || last in other
}
