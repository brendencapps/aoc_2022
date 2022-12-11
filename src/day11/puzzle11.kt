package day11

import Puzzle
import PuzzleInput
import java.io.File
import java.util.PriorityQueue

fun day11Puzzle() {
    Day11PuzzleSolution().solve(Day11PuzzleInput("inputs/day11/example.txt", 10605))
    Day11PuzzleSolution().solve(Day11PuzzleInput("inputs/day11/input.txt", 120056))
    Day11Puzzle2Solution().solve(Day11PuzzleInput("inputs/day11/example.txt", 2713310158))
    Day11Puzzle2Solution().solve(Day11PuzzleInput("inputs/day11/input.txt", 21816744824))
}

data class Monkey(val input: String) {
    private val items = mutableListOf<Long>()
    private val operation: String
    val test: Long
    private val monkeyIfTrue: Int
    private val monkeyIfFalse: Int
    var itemsInspected = 0.toLong()

    init {
        val inputLines = input.lines()
        inputLines[1].substring("  Starting items: ".length).split(", ").forEach { item ->
            items.add(item.toLong())
        }
        operation = inputLines[2].substring("  Operation: new = ".length)
        test = inputLines[3].substring("  Test: divisible by ".length).toLong()
        monkeyIfTrue = inputLines[4].substring("    If true: throw to monkey ".length).toInt()
        monkeyIfFalse = inputLines[5].substring("    If false: throw to monkey ".length).toInt()
    }

    fun inspectItems(monkeys: List<Monkey>) {
        itemsInspected += items.size
        items.forEach { item ->
            val operationParts = operation.replace("old", item.toString()).split(" ")
            val itemToThrow = if(operationParts[1] == "+") {
                (operationParts[0].toLong() + operationParts[2].toLong()) / 3
            }
            else if(operationParts[1] == "*") {
                (operationParts[0].toLong() * operationParts[2].toLong()) / 3
            }
            else {
                item
            }
            if(itemToThrow % test == 0.toLong()) {
                monkeys[monkeyIfTrue].catchItem(itemToThrow)
            }
            else {
                monkeys[monkeyIfFalse].catchItem(itemToThrow)
            }
        }
        items.clear()
    }

    fun inspectItems2(monkeys: List<Monkey>, normalizer: Long) {
        itemsInspected += items.size
        items.forEach { item ->
            val itemNormalized = item % normalizer
            val operationParts = operation.replace("old", itemNormalized.toString()).split(" ")
            val itemToThrow = if(operationParts[1] == "+") {
                (operationParts[0].toLong() + operationParts[2].toLong())
            }
            else if(operationParts[1] == "*") {
                (operationParts[0].toLong() * operationParts[2].toLong())
            }
            else {
                itemNormalized
            }
            if(itemToThrow % test == 0.toLong()) {
                monkeys[monkeyIfTrue].catchItem(itemToThrow)
            }
            else {
                monkeys[monkeyIfFalse].catchItem(itemToThrow)
            }
        }
        items.clear()
    }

    fun catchItem(item: Long) {
        items.add(item)
    }
}

class Day11PuzzleInput(val input: String, expectedResult: Long? = null) : PuzzleInput<Long>(expectedResult) {
    val monkeys = mutableListOf<Monkey>()
    var normalizer = 1.toLong()
    init {
        File(input).readText().split("\r\n\r\n").forEach() { monkey ->
            monkeys.add(Monkey(monkey))
        }
        monkeys.forEach { monkey ->
            normalizer *= monkey.test
        }
    }
}

class Day11PuzzleSolution : Puzzle<Long, Day11PuzzleInput>() {
    override fun solution(input: Day11PuzzleInput): Long {

        for(i in 1 .. 20) {
            input.monkeys.forEach { monkey ->
                monkey.inspectItems(input.monkeys)
            }
        }
        val topMonkeys = PriorityQueue<Long>()
        input.monkeys.forEach {
            topMonkeys.add(it.itemsInspected)
            if(topMonkeys.size > 2) {
                topMonkeys.poll()
            }
        }

        return topMonkeys.poll() * topMonkeys.poll()
    }

}

class Day11Puzzle2Solution : Puzzle<Long, Day11PuzzleInput>() {
    override fun solution(input: Day11PuzzleInput): Long {
        for(i in 1 .. 10000) {
            input.monkeys.forEach { monkey ->
                monkey.inspectItems2(input.monkeys, input.normalizer)
            }
        }
        val topMonkeys = PriorityQueue<Long>()
        input.monkeys.forEach {
            topMonkeys.add(it.itemsInspected)
            if(topMonkeys.size > 2) {
                topMonkeys.poll()
            }
        }

        return topMonkeys.poll() * topMonkeys.poll()
    }

}