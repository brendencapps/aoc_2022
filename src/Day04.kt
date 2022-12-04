import java.io.File

fun main() {
    check(day4Puzzle1("day4_example.txt") == 2)
    println(day4Puzzle1("day4_input.txt"))
    check(day4Puzzle2("day4_example.txt") == 4)
    println(day4Puzzle2("day4_input.txt"))
}

fun computePuzzle(input: String, computeScore: (IntRange, IntRange) -> Int): Int {
    val inputParser = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()

    return File(input).readLines().sumOf { elfPair ->
        val result = inputParser.find(elfPair)
        val (start1, end1, start2, end2) = result!!.destructured
        computeScore(start1.toInt() .. end1.toInt(), start2.toInt() .. end2.toInt())
    }
}

fun day4Puzzle1(input: String): Int {
    return computePuzzle(input) { elf1, elf2 ->
        if(elf1.contains(elf2) || elf2.contains(elf1)) {
            1
        }
        else {
            0
        }
    }
}

fun day4Puzzle2(input: String): Int {
    return computePuzzle(input) { elf1, elf2 ->
        if(elf1.overlaps(elf2)) {
            1
        }
        else {
            0
        }
    }
}

fun IntRange.contains(other: IntRange): Boolean {
    return first >= other.first && last <= other.last
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return other.first in this || other.last in this || first in other || last in other
}
