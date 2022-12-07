package day2

import Puzzle
import PuzzleInput
import java.io.File

sealed class Gesture(val value: Int) {
    object Rock : Gesture(1)
    object Paper : Gesture(2)
    object Scissors : Gesture(3)

    private fun win(opponent: Gesture): Boolean {
        return when(this) {
            Rock -> opponent == Scissors
            Paper -> opponent == Rock
            Scissors -> opponent == Paper
        }
    }

    fun getResult(opponent: Gesture): Result {
        return if(opponent == this) {
            Result.Draw
        } else if(win(opponent)) {
            Result.Win
        } else {
            Result.Lose
        }

    }
}

sealed class Result(val value: Int) {
    object Win : Result(6)
    object Draw : Result(3)
    object Lose : Result(0)

    fun getPlayForResult(opponent: Gesture) : Gesture {
        return when(this) {
            Win ->
                when(opponent) {
                    Gesture.Rock -> Gesture.Paper
                    Gesture.Scissors -> Gesture.Rock
                    Gesture.Paper -> Gesture.Scissors
                }
            Draw -> opponent
            Lose ->
                when(opponent) {
                    Gesture.Rock -> Gesture.Scissors
                    Gesture.Scissors -> Gesture.Paper
                    Gesture.Paper -> Gesture.Rock
                }

        }
    }
}

data class GamePlay (
    val opponent: String,
    val me: String = "",
    val result: String = "") {

    private val _opponent: Gesture
    private val _me: Gesture
    private val _result: Result

    init {
        if(me.isBlank() && result.isBlank()) {
            error("Invalid input.  Expecting my gesture or result to be set.")
        }
        _opponent = getGesture(opponent)
        if(me.isBlank()) {
            _result = getResult(result)
            _me = _result.getPlayForResult(_opponent)
        }
        else if(result.isBlank()) {
            _me = getGesture(me)
            _result = _me.getResult(_opponent)
        }
        else {
            error("Expecting my gesture or result to be blank.")
        }

    }

    private fun getResult(strategy: String) : Result {
        return when(strategy) {
            "X" -> Result.Lose
            "Y" -> Result.Draw
            "Z" -> Result.Win
            else -> error("day2.Result input expected to be X, Y, or Z")
        }
    }

    private fun getGesture(play: String) : Gesture {
        return when(play) {
            "A" -> Gesture.Rock
            "B" -> Gesture.Paper
            "C" -> Gesture.Scissors
            "X" -> Gesture.Rock
            "Y" -> Gesture.Paper
            "Z" -> Gesture.Scissors
            else -> error("day2.Gesture expected to be A, B, C, X, Y, or Z")
        }
    }
    fun getScore(): Int {
        return _me.value + _result.value
    }
}


fun day2Puzzle() {
    Day2Puzzle1Solution().solve(Day2PuzzleInput(10624))
    Day2Puzzle2Solution().solve(Day2PuzzleInput(14060))
    Day2Puzzle1Solution2().solve(Day2PuzzleInput(10624))
    Day2Puzzle2Solution2().solve(Day2PuzzleInput(14060))
}

class Day2PuzzleInput(expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    fun getResult(result: (String, String) -> Int): Int {
        return File("inputs/day2/strategy.txt").readLines().sumOf { game ->
            val gamePlay = game.split(" ")
            check(gamePlay.size == 2)
            result(gamePlay[0], gamePlay[1])
        }
    }
    fun getResult2(result: (String) -> Int): Int {
        return File("inputs/day2/strategy.txt").readLines().sumOf { game ->
            result(game)
        }
    }
}

class Day2Puzzle1Solution : Puzzle<Int, Day2PuzzleInput>() {
    override fun solution(input: Day2PuzzleInput): Int {
        return input.getResult { opponent, me ->
            GamePlay(opponent = opponent, me = me).getScore()
        }
    }
}

class Day2Puzzle2Solution : Puzzle<Int, Day2PuzzleInput>() {
    override fun solution(input: Day2PuzzleInput): Int {
        return input.getResult { opponent, resultStrategy ->
            GamePlay(opponent = opponent, result = resultStrategy).getScore()
        }
    }
}

class Day2Puzzle1Solution2 : Puzzle<Int, Day2PuzzleInput>() {
    override fun solution(input: Day2PuzzleInput): Int {
        return input.getResult2 { game ->
            when(game) {
                "A X" -> 1 + 3
                "A Y" -> 2 + 6
                "A Z" -> 3 + 0
                "B X" -> 1 + 0
                "B Y" -> 2 + 3
                "B Z" -> 3 + 6
                "C X" -> 1 + 6
                "C Y" -> 2 + 0
                "C Z" -> 3 + 3
                else -> 0
            }
        }
    }
}

class Day2Puzzle2Solution2 : Puzzle<Int, Day2PuzzleInput>() {
    override fun solution(input: Day2PuzzleInput): Int {
        return input.getResult2 { game ->
            when (game) {
                "A X" -> 3 + 0
                "A Y" -> 1 + 3
                "A Z" -> 2 + 6
                "B X" -> 1 + 0
                "B Y" -> 2 + 3
                "B Z" -> 3 + 6
                "C X" -> 2 + 0
                "C Y" -> 3 + 3
                "C Z" -> 1 + 6
                else -> 0
            }
        }
    }
}




