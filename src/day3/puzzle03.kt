import java.io.File

fun day3Puzzle() {
    val inputDir = "inputs/day3"
    check(day3Puzzle1("$inputDir/example.txt") == 157)
    println(day3Puzzle1("$inputDir/input.txt")) // 8039
    check(day3Puzzle2("$inputDir/example.txt") == 70)
    println(day3Puzzle2("$inputDir/input.txt")) // 2510
}

fun day3Puzzle1(input: String): Int {
    return File(input).readLines().sumOf { rucksack ->
        val common = rucksack.substring(0 until rucksack.length / 2)
            .toSet()
            .intersect(rucksack.substring(rucksack.length / 2).toSet())
        check(common.size == 1)
        common.first().priority()
    }
}

fun Char.priority(): Int {
    check(isLetter())
    return if(isLowerCase()) {
        this - 'a' + 1
    }
    else {
        this - 'A' + 27
    }
}

fun day3Puzzle2(input: String): Int {
    return File(input)
        .readLines()
        .chunked(3)
        .sumOf { rucksacks ->
            var set = rucksacks[0].toSet()
            for(i in 1 until rucksacks.size) {
                set = set.intersect(rucksacks[i].toSet())
            }
            check(set.size == 1)
            set.first().priority()
        }
}