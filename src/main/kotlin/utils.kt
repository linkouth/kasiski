fun gcd(numbers: List<Int>): Int {
    var currentGCD = numbers[0].toBigInteger().gcd(numbers[1].toBigInteger())
    numbers.subList(2, numbers.lastIndex).forEach {
        currentGCD = currentGCD.gcd(it.toBigInteger())
    }
    return currentGCD.toInt()
}