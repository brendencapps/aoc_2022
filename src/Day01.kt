import java.io.File
import java.lang.Integer.max

fun main() {

    // Make sure we get the correct answer from the example.
    check(getMaxCalories("exampleData.txt") == 24000)

    // Print out the max.
    println(getMaxCalories("caloriesInput.txt"))
}

fun getMaxCalories(input: String): Int {
    var maxCalories = 0
    var currentCalories = 0
    for(calorie in File(input).readLines()) {
        if(calorie.isBlank()) {
            maxCalories = max(currentCalories, maxCalories)
            currentCalories = 0
        }
        else {
            currentCalories += calorie.toInt()
        }
    }
    return max(currentCalories, maxCalories)
}
