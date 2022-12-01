import java.io.File
import java.util.PriorityQueue

fun main() {

    val exampleData = CalorieReader("exampleData.txt")
    val testData = CalorieReader("caloriesInput.txt")

    // Make sure we get the correct answer from the example.
    check(exampleData.getCaloriesForTopX(1) == 24000)
    check(exampleData.getCaloriesForTopX(2) == 35000)
    check(exampleData.getCaloriesForTopX(3) == 45000)
    check(exampleData.getCaloriesForTopX(4) == 51000)
    check(exampleData.getCaloriesForTopX(5) == 55000)
    check(testData.getCaloriesForTopX(1) == 66487)

    for(i in 1 .. 5) {
        check(exampleData.getCaloriesForTopX(i) == exampleData.getCaloriesForTopX2(i))
    }

    // Print out the max.
    println(testData.getCaloriesForTopX(1))
    println(testData.getCaloriesForTopX(3))
}

class CalorieReader(input: String) {

    private val calorieList = File(input)
        .readText()
        .split("\r\n\r\n")
        .map { elf -> elf.lines().sumOf { calories -> calories.toInt() } }

    fun getCaloriesForTopX(numElves: Int): Int {
        val topCalories = PriorityQueue<Int>()
        for(calories in calorieList) {
            topCalories.add(calories)
            if(topCalories.size > numElves) { topCalories.poll() }
        }
        return topCalories.sum()
    }

    fun getCaloriesForTopX2(numElves: Int): Int {
        return calorieList
            .sortedDescending()
            .take(numElves)
            .sum()
    }
}

