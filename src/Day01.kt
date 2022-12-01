import java.io.File

fun main() {

    // Make sure we get the correct answer from the example.
    check(getCaloriesForTopX("exampleData.txt", 1) == 24000)
    check(getCaloriesForTopX("exampleData.txt", 2) == 35000)
    check(getCaloriesForTopX("exampleData.txt", 3) == 45000)
    check(getCaloriesForTopX("exampleData.txt", 4) == 51000)
    check(getCaloriesForTopX("exampleData.txt", 5) == 55000)
    check(getCaloriesForTopX("caloriesInput.txt", 1) == 66487)

    // Print out the max.
    println(getCaloriesForTopX("caloriesInput.txt", 3))
}

fun getCaloriesForTopX(input: String, numElves: Int): Int {

    val maxCalories = IntArray(numElves) { 0 }
    //var maxCalories = 0
    var currentCalories = 0
    for(calorie in File(input).readLines()) {
        if(calorie.isBlank()) {
            addToSortedArray(maxCalories, currentCalories)
            currentCalories = 0
        }
        else {
            currentCalories += calorie.toInt()
        }
    }
    addToSortedArray(maxCalories, currentCalories)
    return maxCalories.sum()
}

fun addToSortedArray(array: IntArray, value: Int) {
    for(i in array.indices) {
        if(value > array[i]) {

            for(j in array.size - 1 downTo i + 1) {
                array[j] = array[j-1]
            }
            array[i] = value
            break
        }
    }
}
