import java.io.File

fun main() {
    val inputDir = "inputs/day4"
    check(day4Puzzle1("$inputDir/example.txt") == 2)
    println(day4Puzzle1("$inputDir.input.txt"))
    check(day4Puzzle2("$inputDir/example.txt") == 4)
    println(day4Puzzle2("$inputDir/input.txt"))
}

fun computePuzzle(input: String, count: (IntRange, IntRange) -> Boolean): Int {
    val inputParser = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()

    return File(input).readLines().count { elfPair ->
        val (start1, end1, start2, end2) = inputParser.find(elfPair)!!.destructured
        count(start1.toInt() .. end1.toInt(), start2.toInt() .. end2.toInt())
    }
}

fun day4Puzzle1(input: String): Int {
    return computePuzzle(input) { elf1, elf2 ->
        elf1.fullyContains(elf2) || elf2.fullyContains(elf1)
    }
}

fun day4Puzzle2(input: String): Int {
    return computePuzzle(input) { elf1, elf2 ->
        elf1.overlaps(elf2)
    }
}

fun IntRange.fullyContains(other: IntRange): Boolean {
    return first >= other.first && last <= other.last
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return other.first in this || other.last in this || first in other || last in other
}
