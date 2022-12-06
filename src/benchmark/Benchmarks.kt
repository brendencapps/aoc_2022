package benchmark

import day6Puzzle
import day6Puzzle2
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@ExperimentalStdlibApi
@State(Scope.Benchmark)
class TestBenchmark {
    @Benchmark
    fun day1Puzzle1Set() {
        day6Puzzle("inputs/day6/input.txt", 4)
    }

    @Benchmark
    fun day1Puzzle2Set() {
        day6Puzzle("inputs/day6/input.txt", 14)
    }
    @Benchmark
    fun day1Puzzle1Array() {
        day6Puzzle2("inputs/day6/input.txt", 4)
    }

    @Benchmark
    fun day1Puzzle2Array() {
        day6Puzzle2("inputs/day6/input.txt", 14)
    }
}