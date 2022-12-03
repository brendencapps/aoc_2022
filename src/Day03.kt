import java.io.File

fun main() {
    val result = day3Puzzle1("day3_example.txt")
    println(result)
    check(result == 157)
    println(day3Puzzle1("day3_input.txt")) // 8039

    val result2 = day3Puzzle2("day3_example.txt")
    println(result2)
    check(result2 == 70)
    println(day3Puzzle2("day3_input.txt")) // 2510
}

fun day3Puzzle1(input: String): Int {
    return File(input).readLines().sumOf { rucksack ->
        val common = rucksack
            .slice(0 until rucksack.length / 2)
            .toSet()
            .intersect(rucksack
                .slice(rucksack.length / 2  until rucksack.length)
                .toSet())
        check(common.size == 1)
        letterToPriority(common.first())
    }
}

fun letterToPriority(letter: Char): Int {
    check(letter.isLetter())
    return if(letter.isLowerCase()) {
        letter - 'a' + 1
    }
    else {
        letter - 'A' + 27
    }
}

fun day3Puzzle2(input: String): Int {

    var index = 0
    var currentSet = emptySet<Char>()
    return File(input)
        .readLines()
        .sumOf { rucksack ->
            currentSet = if(currentSet.isEmpty()) {
                rucksack.toSet()
            } else {
                currentSet.intersect(rucksack.toSet())
            }
            index++
            if(index % 3 == 0) {
                check(currentSet.size == 1)
                val letter = currentSet.first()
                currentSet = emptySet()
                letterToPriority(letter)
            }
            else {
                0
            }
        }
}