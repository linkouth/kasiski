class Permutations {
    companion object {
        fun encryptText(text: String, key: List<Int>): String {
            val modifiedText = text + " ".repeat(key.size - text.length % key.size)
            var result = ""

            val batchSize = key.size
            val batchNumbers = modifiedText.length / batchSize
            for (idx in 0 until batchNumbers) {
                val textBatch = modifiedText.subSequence(idx * batchSize, (idx + 1) * batchSize)
                result += key.map { pos -> textBatch[pos - 1] }.joinToString("")
            }

            return result
        }

        fun decryptText(encryptedText: String, key: List<Int>): String {
            var result = ""

            val batchSize = key.size
            val batchNumbers = encryptedText.length / batchSize
            for (idx in 0 until batchNumbers) {
                val textBatch = encryptedText.subSequence(idx * batchSize, (idx + 1) * batchSize)
                val decryptedBatch = textBatch.map { it }.toMutableList()
                key.forEachIndexed { index, pos ->
                    decryptedBatch[pos - 1] = textBatch[index]
                }
                result += decryptedBatch.joinToString("")
            }

            return result
        }

        private fun isCorrectPermutation(permutation: List<Int>): Boolean {
            val used = MutableList(permutation.size) { false }
            val first = permutation.first()
            var curr = first
            var i = 0
            while (i < permutation.size && curr != permutation[curr - 1] && !used[curr - 1]) {
                i++
                used[curr - 1] = true
                curr = permutation[curr - 1]
            }

            return i == permutation.size && used.all { it }
        }

        private fun allPermutations(list: List<Int>): List<List<Int>> {
            if (list.isEmpty()) return emptyList()

            fun <Int> recAllPermutations(_list: List<Int>): List<List<Int>> {
                if (_list.isEmpty()) return listOf(emptyList())

                val result: MutableList<List<Int>> = mutableListOf()
                for (i in _list.indices) {
                    recAllPermutations(_list - _list[i]).forEach{
                            item -> result.add(item + _list[i])
                    }
                }
                return result
            }

            return recAllPermutations(list).filter { isCorrectPermutation(it) }
        }

        fun getKey(keyLength: Int): List<Int> {
            return allPermutations(List(keyLength) { it + 1 }).random()
        }
    }
}