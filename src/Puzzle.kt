open class PuzzleInput<T>(val expectedResult: T? = null)
abstract class Puzzle<T, P : PuzzleInput<T>> {
    fun solve(input: P) {
        val solution = solution(input)
        if(input.expectedResult != null) {
            if(solution != input.expectedResult) {
                println("$solution == ${input.expectedResult}")
            }
            check(solution == input.expectedResult)
        }
        else {
            println(solution)
        }
    }

    abstract fun solution(input: P) : T
}