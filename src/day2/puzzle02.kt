import java.io.File
import kotlin.system.measureTimeMillis

sealed class Gesture(val value: Int) {
    object Rock : Gesture(1)
    object Paper : Gesture(2)
    object Scissors : Gesture(3)

    private fun win(opponent: Gesture): Boolean {
        return when(this) {
            Rock -> opponent == Scissors
            Paper -> opponent == Rock
            Scissors -> opponent == Paper
            else -> error("Unknown gesture")
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
                    else -> error("Unknown gesture")
                }
            Draw -> opponent
            Lose ->
                when(opponent) {
                    Gesture.Rock -> Gesture.Scissors
                    Gesture.Scissors -> Gesture.Paper
                    Gesture.Paper -> Gesture.Rock
                    else -> error("Unknown gesture")
                }
            else -> error("Unknown outcome")

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
            else -> error("Result input expected to be X, Y, or Z")
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
            else -> error("Gesture expected to be A, B, C, X, Y, or Z")
        }
    }


    fun getScore(): Int {
        return _me.value + _result.value
    }
}

fun main() {
    sanityCheck()
    val timeInMillisP1 = measureTimeMillis {
        puzzle1() // 10624
    }
    val timeInMillisP2 = measureTimeMillis {
        puzzle2() // 14060
    }

    println("$timeInMillisP1 $timeInMillisP2")
}

fun playGame(play: (String, String) -> Int) {
    println(
        File("inputs/day2/strategy.txt").readLines().sumOf { game ->
            val gamePlay = game.split(" ")
            check(gamePlay.size == 2)
            play(gamePlay[0], gamePlay[1])
        }
    )
}

fun puzzle1() {
    playGame { opponent: String, me: String ->
        GamePlay(opponent = opponent, me = me).getScore()
    }
}

fun puzzle2() {
    playGame { opponent: String, resultStrategy: String ->
        GamePlay(opponent = opponent, result = resultStrategy).getScore()
    }
}


fun sanityCheck() {
    check(Gesture.Rock.value + Result.Win.value == 7)
    check(Gesture.Rock.value + Result.Lose.value == 1)
    check(Gesture.Rock.value + Result.Draw.value == 4)
    check(Gesture.Paper.value + Result.Win.value == 8)
    check(Gesture.Paper.value + Result.Lose.value == 2)
    check(Gesture.Paper.value + Result.Draw.value == 5)
    check(Gesture.Scissors.value + Result.Win.value == 9)
    check(Gesture.Scissors.value + Result.Lose.value == 3)
    check(Gesture.Scissors.value + Result.Draw.value == 6)

}

