package day15

import Puzzle
import PuzzleInput
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.math.abs

fun day15Puzzle() {
    Day15PuzzleSolution().solve(Day15PuzzleInput("inputs/day15/example.txt",10, 26))
    Day15PuzzleSolution().solve(Day15PuzzleInput("inputs/day15/example.txt",9, 25))
    Day15PuzzleSolution().solve(Day15PuzzleInput("inputs/day15/example.txt",11, 28))
    Day15PuzzleSolution().solve(Day15PuzzleInput("inputs/day15/input.txt", 2000000, 5525990))
    Day15Puzzle2Solution().solve(Day15PuzzleInput("inputs/day15/example.txt", 20, 56000011))
    Day15Puzzle2Solution().solve(Day15PuzzleInput("inputs/day15/input.txt", 4000000, 11756174628223))
}

data class RangeOfInterest(val range: IntRange, val hasBeacon: Boolean) : Comparable<RangeOfInterest> {
    override fun compareTo(other: RangeOfInterest): Int {
        return if(range.first < other.range.first) {
            -1
        }
        else if(range.first == other.range.first) {
            if(range.last > other.range.last) {
                -1
            } else if(other.range.last == range.last) {
                0
            }
            else {
                1
            }
        }
        else {
            1
        }
    }
}

data class Beacon(val x: Int, val y: Int)
data class Sensor(val x: Int, val y: Int, val nearestBeacon: Beacon) {
    val distance = abs(y - nearestBeacon.y) + abs(x - nearestBeacon.x)
    fun pointsOfInterestRange(rowOfInterest: Int ): RangeOfInterest? {
        IntRange(y - distance, y + distance).contains(nearestBeacon.y)
        if(IntRange(y - distance, y + distance).contains(rowOfInterest)) {
            val range = distance - abs(rowOfInterest - y)
            val r = IntRange(x - range, x + range)
            if(r.contains(nearestBeacon.x) && nearestBeacon.y == rowOfInterest) {
                // beacon must be at one of the ends.  Trim the range.
                if(r.first == r.last) {
                    return null
                }
                else if(nearestBeacon.x == r.first) {
                    return RangeOfInterest(IntRange(r.first + 1, r.last), true)
                }
                else {
                    return RangeOfInterest(IntRange(r.first, r.last - 1), true)
                }
            }
            else {
                return RangeOfInterest(r, false)
            }
        }
        return null
    }
}

class Day15PuzzleInput(val input: String, val rowOfInterest: Int, expectedResult: Long? = null) : PuzzleInput<Long>(expectedResult) {
    val sensorList: List<Sensor> =
        File(input).readLines().map { line ->
            val parser = "Sensor at x=(-?\\d*), y=(-?\\d*): closest beacon is at x=(-?\\d*), y=(-?\\d*)".toRegex()
            val matcher = parser.find(line) ?: error("regex issue with $line")
            val (sx, sy, bx, by) = matcher.destructured
            Sensor(sx.toInt(), sy.toInt(), Beacon(bx.toInt(), by.toInt()))
        }
}


class Day15PuzzleSolution : Puzzle<Long, Day15PuzzleInput>() {

    override fun solution(input: Day15PuzzleInput): Long {

        val ranges = input.sensorList.mapNotNull { sensor ->
            sensor.pointsOfInterestRange(input.rowOfInterest)
        }.sorted()

        var sum = 0
        var previousRange: IntRange? = null
        for(i in ranges.indices) {
            if(previousRange == null) {
                sum += ranges[i].range.last - ranges[i].range.first + 1
                previousRange = ranges[i].range
            }
            else {
                if(ranges[i].range.last <= ranges[i-1].range.last) {
                    // Contains, don't add.
                }
                else if(ranges[i].range.first <= previousRange.last) {
                    // Overlaps
                    previousRange = IntRange(previousRange.last + 1, ranges[i].range.last)
                    sum += previousRange.last - previousRange.first + 1
                }
                else {
                    sum += ranges[i].range.last - ranges[i].range.first + 1
                    previousRange = ranges[i].range
                }
            }

        }
        return sum.toLong()
    }

}


class Day15Puzzle2Solution : Puzzle<Long, Day15PuzzleInput>() {

    override fun solution(input: Day15PuzzleInput): Long {

        var answer: Long = 0
        runBlocking {
            for (y in 0..input.rowOfInterest) {
                launch {
                    val ranges = input.sensorList.mapNotNull { sensor ->
                        sensor.pointsOfInterestRange(y)
                    }.sorted()
                    var range: IntRange? = null
                    for (i in ranges.indices) {
                        if (range == null) {
                            range = ranges[i].range
                        } else {
                            if (ranges[i].range.first > range.last) {
                                for (x in range.last + 1 until ranges[i].range.first) {
                                    if (!input.sensorList.any { (it.x == x && it.y == y) || (it.nearestBeacon.x == x && it.nearestBeacon.y == y) }) {
                                        // We have found a gap.
                                        println("Potential distress at $x $y")
                                        answer = x.toLong() * 4000000 + y.toLong()
                                        println("$answer")
                                        return@launch
                                    }
                                }
                            }
                            if (ranges[i].range.last > range.last) {
                                range = IntRange(range.first, ranges[i].range.last)
                            }
                        }

                    }
                }
            }
        }
        return answer
    }

}