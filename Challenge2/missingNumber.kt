fun main() {
    val arr = arrayOf(3, 4, 6, 1, 7, 2)
    println(missingNumber(arr))
}

fun missingNumber(arr: Array<Int>): Int {
    // Range is from 1 to n + 1
    val range = arr.size + 1
    // The missing number is (sum of all numbers ranging from 1 to n + 1) - (sum of all numbers in arr)
    val missingNumber = range * (range + 1) / 2 - arr.sum()
    return missingNumber
}