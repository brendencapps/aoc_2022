import java.io.File

fun main() {
    check(day6Puzzle("inputs/day6/example.txt", 4) == 7)
    check(day6Puzzle("inputs/day6/example2.txt", 4) == 5)
    check(day6Puzzle("inputs/day6/example3.txt", 4) == 6)
    check(day6Puzzle("inputs/day6/example4.txt", 4) == 10)
    check(day6Puzzle("inputs/day6/example5.txt", 4) == 11)
    println(day6Puzzle("inputs/day6/input.txt", 4)) // 1760


    check(day6Puzzle("inputs/day6/example.txt", 14) == 19)
    check(day6Puzzle("inputs/day6/example2.txt", 14) == 23)
    check(day6Puzzle("inputs/day6/example3.txt", 14) == 23)
    check(day6Puzzle("inputs/day6/example4.txt", 14) == 29)
    check(day6Puzzle("inputs/day6/example5.txt", 14) == 26)
    println(day6Puzzle("inputs/day6/input.txt", 14)) // 2974
}

fun day6Puzzle(input: String, numMarkers: Int): Int {
    val message = File(input).readText()
    check(message.length >= numMarkers)
    val code = mutableSetOf<Char>()

    for(i in message.indices) {
        if(code.contains(message[i])) {
            val it = code.iterator()
            while(it.hasNext()) {
                if(it.next() == message[i]) {
                    it.remove()
                    break
                }
                it.remove()
            }
        }
        code.add(message[i])
        if(code.size == numMarkers) {
            return i + 1
        }
    }

    error("Did not find start of stream")
}
