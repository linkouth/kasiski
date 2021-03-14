import kotlin.math.min
import kotlin.math.sqrt

class Kasiski {
    companion object {
        fun examination(cipherText: String, sequenceLength: Int, maxKeyLength: Int): Pair<Int, Map<Int, Int>> {
            val sequencePositions = mutableMapOf<String, MutableList<Int>>()

            for (idx in 0.until(cipherText.length - sequenceLength)) {
                if (sequencePositions[cipherText.substring(idx, idx + sequenceLength)] == null) {
                    sequencePositions[cipherText.substring(idx, idx + sequenceLength)] = mutableListOf(idx)
                } else {
                    sequencePositions[cipherText.substring(idx, idx + sequenceLength)]?.add(idx)
                }
            }

            val filteredSequencePositions = sequencePositions.filter { it.value.size > 1 }
            assert(filteredSequencePositions.isNotEmpty())

            val sequenceSpacings = mutableMapOf<String, MutableList<Int>>().withDefault { mutableListOf() }

            for ((seq, positions) in filteredSequencePositions) {
                for ((a, b) in positions.zip(positions.subList(1, positions.lastIndex))) {
                    if (sequenceSpacings[seq] == null) {
                        sequenceSpacings[seq] = mutableListOf(b - a)
                    } else {
                        sequenceSpacings[seq]?.add(b - a)
                    }
                }
            }

            val factorCount = mutableMapOf<Int, Int>().withDefault { 0 }
            for (spacings in sequenceSpacings.values) {
                for (space in spacings) {
                    for (f in (2).until(min(maxKeyLength, sqrt(space.toDouble()).toInt() + 1))) {
                        if (space % f == 0) {
                            factorCount[f] = factorCount[f]?.plus(1) ?: 1
                        }
                    }
                }
            }

            return gcd(sequenceSpacings.values.flatten()) to factorCount.toMap()
        }
    }
}