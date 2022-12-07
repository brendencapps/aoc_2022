package benchmark

import Day6PuzzleInput
import Day6PuzzleSolutionMutableSet
import Day6PuzzleSolutionArray
import Day7Puzzle2Solution
import Day7Puzzle2SolutionList
import Day7PuzzleInput
import Day7PuzzleInputList
import Day7PuzzleSolution
import Day7PuzzleSolutionList
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@ExperimentalStdlibApi
@State(Scope.Benchmark)
class TestBenchmark {

    /*
    @Benchmark
    fun day6SSetI1() {
        Day6PuzzleSolutionMutableSet().solve(Day6PuzzleInput("inputs/day6/input.txt", 4, 1760))
    }

    @Benchmark
    fun day6SSetI2() {
        Day6PuzzleSolutionMutableSet().solve(Day6PuzzleInput("inputs/day6/input.txt", 14, 2974))
    }
    @Benchmark
    fun day6SArrayI1() {
        Day6PuzzleSolutionArray().solve(Day6PuzzleInput("inputs/day6/input.txt", 4, 1760))
    }

    @Benchmark
    fun day6SArrayI2() {
        Day6PuzzleSolutionArray().solve(Day6PuzzleInput("inputs/day6/input.txt", 14, 2974))
    }
*/
    @Benchmark
    fun day7P1OrigExample() {
        Day7PuzzleSolution(100000).solve(Day7PuzzleInput("inputs/day7/exampleCommands.txt", 95437))
    }
    @Benchmark
    fun day7P1OrigInput() {
        Day7PuzzleSolution(100000).solve(Day7PuzzleInput("inputs/day7/inputCommands.txt", 1428881))
    }
    @Benchmark
    fun day7P1ListExample() {
        Day7PuzzleSolutionList(100000).solve(Day7PuzzleInputList("inputs/day7/exampleCommands.txt", 95437))
    }
    @Benchmark
    fun day7P1ListInput() {
        Day7PuzzleSolutionList(100000).solve(Day7PuzzleInputList("inputs/day7/inputCommands.txt",1428881))
    }
    @Benchmark
    fun day7P2OrigExample() {
        Day7Puzzle2Solution().solve(Day7PuzzleInput("inputs/day7/exampleCommands.txt", 24933642))
    }
    @Benchmark
    fun day7P2OrigInput() {
        Day7Puzzle2Solution().solve(Day7PuzzleInput("inputs/day7/inputCommands.txt", 10475598))
    }
    @Benchmark
    fun day7P2ListExample() {
        Day7Puzzle2SolutionList().solve(Day7PuzzleInputList("inputs/day7/exampleCommands.txt", 24933642))
    }
    @Benchmark
    fun day7P2ListInput() {
        Day7Puzzle2SolutionList().solve(Day7PuzzleInputList("inputs/day7/inputCommands.txt", 10475598))
    }
}