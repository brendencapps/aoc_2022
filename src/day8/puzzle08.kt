package day8

import Puzzle
import PuzzleInput
import java.io.File
import java.lang.Integer.max

data class TreeProperties(val height: Int, private val initialScore: Int) {
    var top = initialScore
    var bottom = initialScore
    var left = initialScore
    var right = initialScore

    fun getTop(treeAbove: TreeProperties) {
        top = max(treeAbove.top, treeAbove.height)
    }

    fun getLeft(treeLeft: TreeProperties) {
        left = max(treeLeft.left, treeLeft.height)
    }
    fun getRight(treeRight: TreeProperties) {
        right = max(treeRight.right, treeRight.height)
    }
    fun getBottom(treeBottom: TreeProperties) {
        bottom = max(treeBottom.bottom, treeBottom.height)
    }

    fun visible(): Boolean {
        return height > left || height > right || height > bottom || height > top
    }
}

fun day8Puzzle() {

    Day8PuzzleSolution().solve(Day8PuzzleInput("inputs/day8/example.txt", -1, 21))
    Day8PuzzleSolution().solve(Day8PuzzleInput("inputs/day8/input.txt", -1, 1851))
    Day8Puzzle2Solution().solve(Day8PuzzleInput("inputs/day8/example.txt", 0, 8))
    Day8Puzzle2Solution().solve(Day8PuzzleInput("inputs/day8/input.txt", 0, 574080))
}

class Day8PuzzleInput(val input: String, initialScore: Int, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    val trees = File(input).readLines().map { row ->
        row.toCharArray().map {tree ->
            TreeProperties(tree.digitToInt(), initialScore)
        }
    }
}

class Day8PuzzleSolution : Puzzle<Int, Day8PuzzleInput>() {
    override fun solution(input: Day8PuzzleInput): Int {
        for(row in 1 until input.trees.size - 1) {
            val rowReverse = input.trees.size - row - 1
            for(col in 1 until input.trees[row].size - 1) {
                val colReverse = input.trees[row].size - col - 1
                //println("$row $col $rowReverse $colReverse ${input.trees.size} ${input.trees[row].size} ${input.trees[rowReverse].size}")
                input.trees[row][col].getTop(input.trees[row-1][col])
                input.trees[row][col].getLeft(input.trees[row][col-1])
                input.trees[rowReverse][colReverse].getBottom(input.trees[rowReverse+1][colReverse])
                input.trees[rowReverse][colReverse].getRight(input.trees[rowReverse][colReverse+1])
            }
        }
        return input.trees.sumOf { row ->
            row.count { tree ->
                tree.visible()
            }
        }
    }

}

class Day8Puzzle2Solution : Puzzle<Int, Day8PuzzleInput>() {
    override fun solution(input: Day8PuzzleInput): Int {
        for(row in 1 until input.trees.size - 1) {
            for(col in 1 until input.trees[row].size - 1) {
                input.trees[row][col].top = visibleTreesInCol(col, row-1 downTo 0, input.trees[row][col], input)
                input.trees[row][col].left = visibleTreesInRow(row, col-1 downTo 0, input.trees[row][col], input)
                input.trees[row][col].bottom = visibleTreesInCol(col, row + 1 until input.trees.size, input.trees[row][col], input)
                input.trees[row][col].right = visibleTreesInRow(row, col + 1 until input.trees[row].size, input.trees[row][col], input)
            }
        }
        return input.trees.maxOf { row ->
            row.maxOf { tree ->
                tree.top * tree.left * tree.right * tree.bottom
            }
        }

    }

    private fun visibleTreesInRow(row: Int, range: IntProgression, tree: TreeProperties, input: Day8PuzzleInput): Int {
        var trees = 0
        for(col in range) {
            trees++
            if(input.trees[row][col].height >= tree.height) {
                break
            }
        }
        return trees
    }

    private fun visibleTreesInCol(col: Int, range: IntProgression, tree: TreeProperties, input: Day8PuzzleInput): Int {
        var trees = 0
        for(row in range) {
            trees++
            if(input.trees[row][col].height >= tree.height) {
                break
            }
        }
        return trees
    }
}