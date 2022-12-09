package day8

import Puzzle
import PuzzleInput
import java.io.File
import java.lang.Integer.max

data class TreeProperties(val height: Int, val row: Int, val col: Int, private val initialScore: Int) {
    var top = initialScore
    var bottom = initialScore
    var left = initialScore
    var right = initialScore

    fun visible(): Boolean {
        return height > left || height > right || height > bottom || height > top
    }

    fun visibleScore(): Int {
        return top * left * right * bottom
    }
}

class TreeIterator(val input: Day8PuzzleInput) {

    fun iterate(op: (TreeProperties) -> Boolean): Int {
       return iterate(0 until input.rows, 0 until input.cols, op)
    }

    fun iterate(rows: IntProgression, cols: IntProgression, op: (TreeProperties) -> Boolean): Int {
        var index = 0
        for(row in rows) {
            for(col in cols) {
                index++
                if(!op(input.trees[row][col])) {
                    return index
                }
            }
        }
        return index
    }

    fun <T> map(op: (TreeProperties) -> T) : List<T> {
        return map(0 until input.rows, 0 until input.cols, op)
    }

    fun <T> map(rows: IntProgression, cols: IntProgression, op: (TreeProperties) -> T) : List<T> {
        val list = mutableListOf<T>()
        for(row in rows) {
            for(col in cols) {
                list.add(op(input.trees[row][col]))
            }
        }
        return list
    }

    fun <T> left(tree: TreeProperties, op: (TreeProperties) -> T): T {
        return op(input.trees[tree.row][tree.col - 1])
    }

    fun <T> up(tree: TreeProperties, op: (TreeProperties) -> T): T {
        return op(input.trees[tree.row - 1][tree.col])
    }

    fun <T> right(tree: TreeProperties, op: (TreeProperties) -> T): T {
        return op(input.trees[tree.row][tree.col + 1])
    }

    fun <T> down(tree: TreeProperties, op: (TreeProperties) -> T): T {
        return op(input.trees[tree.row + 1][tree.col])
    }

}

fun day8Puzzle() {

    Day8PuzzleSolution().solve(Day8PuzzleInput("inputs/day8/example.txt", -1, 21))
    Day8PuzzleSolution().solve(Day8PuzzleInput("inputs/day8/input.txt", -1, 1851))
    Day8Puzzle2Solution().solve(Day8PuzzleInput("inputs/day8/example.txt", 0, 8))
    Day8Puzzle2Solution().solve(Day8PuzzleInput("inputs/day8/input.txt", 0, 574080))
}

class Day8PuzzleInput(val input: String, initialScore: Int, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    val trees: List<List<TreeProperties>>
    val rows: Int
    val cols: Int
    val iterator: TreeIterator
    init {
        trees = File(input).readLines().mapIndexed { rowIndex, row ->
            row.toCharArray().mapIndexed {colIndex, tree ->
                TreeProperties(tree.digitToInt(), rowIndex, colIndex, initialScore)
            }
        }
        rows = trees.size
        cols = trees[0].size
        iterator = TreeIterator(this)
    }

}

class Day8PuzzleSolution : Puzzle<Int, Day8PuzzleInput>() {
    override fun solution(input: Day8PuzzleInput): Int {
        input.iterator.iterate(1 until input.rows, 1 until input.cols) { tree ->
            tree.top = input.iterator.up(tree) { max(it.top, it.height) }
            tree.left = input.iterator.left(tree) { max(it.left, it.height) }
            true
        }
        input.iterator.iterate(input.rows - 2  downTo 0, input.cols - 2  downTo 0) { tree ->
            tree.bottom = input.iterator.down(tree) { max(it.bottom, it.height) }
            tree.right = input.iterator.right(tree) { max(it.right, it.height) }
            true
        }
        return input.iterator.map(input.trees.indices, input.trees[0].indices) { it.visible() }.count { it }
    }
}

class Day8Puzzle2Solution : Puzzle<Int, Day8PuzzleInput>() {
    override fun solution(input: Day8PuzzleInput): Int {
        input.iterator.iterate { tree ->
            tree.top = input.iterator.iterate(tree.row - 1 downTo 0, tree.col .. tree.col) { otherTree ->
                tree.height > otherTree.height
            }
            tree.left = input.iterator.iterate(tree.row .. tree.row, tree.col - 1 downTo 0) { otherTree ->
                tree.height > otherTree.height
            }
            true
        }

        input.iterator.iterate(input.rows - 1 downTo 0, input.cols - 1 downTo 0) { tree ->
            tree.bottom = input.iterator.iterate(tree.row + 1 until input.rows, tree.col .. tree.col) { otherTree ->
                tree.height > otherTree.height
            }
            tree.right = input.iterator.iterate(tree.row .. tree.row, tree.col + 1 until input.cols) { otherTree ->
                tree.height > otherTree.height
            }
            true
        }

        return input.iterator.map { tree -> tree }.maxOf { it.visibleScore()  }

    }
}