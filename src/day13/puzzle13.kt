package day13

import Puzzle
import PuzzleInput
import org.json.JSONArray
import java.io.File
import java.lang.Integer.min


fun day13Puzzle() {
    Day13PuzzleSolution().solve(Day13PuzzleInput("inputs/day13/example.txt", 13))
    Day13PuzzleSolution().solve(Day13PuzzleInput("inputs/day13/input.txt", 5675))
    Day13Puzzle2Solution().solve(Day13Puzzle2Input("inputs/day13/example2.txt", 140))
    Day13Puzzle2Solution().solve(Day13Puzzle2Input("inputs/day13/input2.txt", 20383))
}

class Packet(val input: String) : Comparable<Packet> {

    private val packet = JSONArray(input)

    override fun compareTo(other: Packet): Int {
        return compare(packet, other.packet)
    }

    private fun compare(me: JSONArray, them: JSONArray): Int {
        for(i in 0 until min(me.length(), them.length())) {
            if(me.get(i) is Int && them[i] is Int) {
                val mine = me.getInt(i)
                val theirs = them.getInt(i)
                if(mine < theirs) return -1
                if(theirs < mine) return 1
            }
            else {
                val result = compare(getJSONArray(me, i), getJSONArray(them, i))
                if(result != 0) return result
            }

        }
        return (me.length() - them.length()).coerceIn(-1, 1)
    }

    private fun getJSONArray(array: JSONArray, index: Int): JSONArray {
        return if(array.get(index) is Int) {
            val newArray = JSONArray()
            newArray.put(array.getInt(index))
            newArray
        } else {
            array.getJSONArray(index)
        }
    }
}

class Day13PuzzleInput(val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    val packetPairs: List<Pair<Packet, Packet>> = File(input).readText().split("\r\n\r\n").map { packetPairs ->
        val packetInput = packetPairs.lines()
        Pair(Packet(packetInput[0]), Packet(packetInput[1]))
    }
}

class Day13Puzzle2Input(val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    val packets: List<Packet> = File(input).readLines().filter { it.isNotBlank() }.map { Packet(it)}
}

class Day13PuzzleSolution : Puzzle<Int, Day13PuzzleInput>() {

    override fun solution(input: Day13PuzzleInput): Int {
        return input.packetPairs.mapIndexed { index, pair ->
            if(pair.first <= pair.second) {
                index + 1
            }
            else {
                0
            }
        }.sum()
    }
}


class Day13Puzzle2Solution : Puzzle<Int, Day13Puzzle2Input>() {
    override fun solution(input: Day13Puzzle2Input): Int {
        var divider1 = 0
        var divider2 = 0
        input.packets.sorted().forEachIndexed { index, packet ->
            //println(packet.input)
            if(packet.input == "[[2]]") {
                divider1 = index + 1
            }
            else if(packet.input == "[[6]]") {
                divider2 = index + 1
            }
        }
        return divider1 * divider2
    }

}