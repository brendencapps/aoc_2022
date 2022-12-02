import java.io.File
import kotlin.system.exitProcess

sealed class Play(val value: Int) {
    object Rock : Play(1)
    object Paper : Play(2)
    object Scissors : Play(3)

    fun getResult(opponent: Play): Result {
        return when(this) {
            Rock ->
                when (opponent) {
                    Rock -> Result.Draw
                    Paper -> Result.Lose
                    Scissors -> Result.Win
                }
            Paper ->
                when (opponent) {
                    Rock -> Result.Win
                    Paper -> Result.Draw
                    Scissors -> Result.Lose
                }
            Scissors ->
                when (opponent) {
                    Rock -> Result.Lose
                    Paper -> Result.Win
                    Scissors -> Result.Draw
                }
        }
    }
}

sealed class Result(val value: Int) {
    object Win : Result(6)
    object Draw : Result(3)
    object Lose : Result(0)

    fun getPlayForResult(opponent: Play) : Play {
        return when(this) {
            Win ->
                when(opponent) {
                    Play.Rock -> Play.Paper
                    Play.Scissors -> Play.Rock
                    Play.Paper -> Play.Scissors
                }
            Draw -> opponent
            Lose ->
                when(opponent) {
                    Play.Rock -> Play.Scissors
                    Play.Scissors -> Play.Paper
                    Play.Paper -> Play.Rock
                }

        }
    }
}

data class GamePlay (
    val opponent: String,
    val me: String = "",
    val result: String = "") {

    private val _opponent: Play
    private val _me: Play
    private val _result: Result

    init {
        if(me.isBlank() && result.isBlank()) {
            exitProcess(-1)
        }
        _opponent = getPlay(opponent)
        if(me.isBlank()) {
            _result = getResult(result)
            _me = _result.getPlayForResult(_opponent)
        }
        else if(result.isBlank()) {
            _me = getPlay(me)
            _result = _me.getResult(_opponent)
        }
        else {
            exitProcess(-1)
        }

    }

    private fun getResult(strategy: String) : Result {
        return when(strategy) {
            "X" -> Result.Lose
            "Y" -> Result.Draw
            "Z" -> Result.Win
            else -> exitProcess(-1)
        }
    }

    private fun getPlay(play: String) : Play {
        return when(play) {
            "A" -> Play.Rock
            "B" -> Play.Paper
            "C" -> Play.Scissors
            "X" -> Play.Rock
            "Y" -> Play.Paper
            "Z" -> Play.Scissors
            else -> exitProcess(-1)
        }
    }


    fun getScore(): Int {
        return _me.value + _result.value
    }
}

fun main() {
    sanityCheck()
    puzzle1() // 10624
    println("------------------------------------------------")
    puzzle2() // 14060
}

fun playGame(play: (String, String) -> Int) {
    var score = 0
    for (game in File("day2Puzzle1Strategy.txt").readLines()) {
        val gamePlay = game.split(" ")
        check(gamePlay.size == 2)
        score += play(gamePlay[0], gamePlay[1])
    }
    println(score)
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
    check(Play.Rock.value + Result.Win.value == 7)
    check(Play.Rock.value + Result.Lose.value == 1)
    check(Play.Rock.value + Result.Draw.value == 4)
    check(Play.Paper.value + Result.Win.value == 8)
    check(Play.Paper.value + Result.Lose.value == 2)
    check(Play.Paper.value + Result.Draw.value == 5)
    check(Play.Scissors.value + Result.Win.value == 9)
    check(Play.Scissors.value + Result.Lose.value == 3)
    check(Play.Scissors.value + Result.Draw.value == 6)

}

