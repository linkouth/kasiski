class MatchesIndex {
    companion object {
        fun matchesIndex(ySeq: String, zSeq: String): Double {
            assert(ySeq.length == zSeq.length)

            return ySeq.reduceIndexed {
                    index, acc, ch -> acc + (if (ch == zSeq[index]) 1 else 0)
            }.toDouble() / ySeq.length
        }

        fun averageMatchesIndex(ySeq: String, zSeq: String, abc:ABC=ABC.ENG): Double {
            assert(ySeq.length == zSeq.length)

            val actualABC = if (abc == ABC.ENG) englishABC else russianABC
            val ySeqFiltered = ySeq.filter { it.isLetter() }.map { it.toUpperCase() }
            val zSeqFiltered = zSeq.filter { it.isLetter() }.map { it.toUpperCase() }

            val yFrequencies = ySeqFiltered.groupingBy { it }.eachCount().mapValues { it.value.toDouble() / ySeq.length }
            val zFrequencies = zSeqFiltered.groupingBy { it }.eachCount().mapValues { it.value.toDouble() / zSeq.length }

            var res = .0
            actualABC.forEach {ch -> res += (yFrequencies[ch] ?: .0) * (zFrequencies[ch] ?: .0) }
            return res
        }
    }
}