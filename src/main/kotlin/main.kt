import kotlin.math.min
import kotlin.math.sqrt

const val digits = "1234567890"
val englishABC = "abcdefghijklmnopqrstuvwxyz$digits".toUpperCase()
val russianABC = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя$digits".toUpperCase()

fun main() {
    val keyLength = 5
    val permutation = List(keyLength) { it + 1 }

    val keys = allPermutations(permutation)
    println("key: $keys")

    val text = "Когда вы впервые собираете деньги на проект, сначала спросите себя: «В нас стоит инвестировать?» Честный ответ на этот вопрос поможет и в следующий раз. Не рекламируйте. Говорите правду, и пусть инвесторы читают по глазам. Есть три условия успешного стартапа: работать с хорошими людьми, делать то, что нужно клиентам, и тратить как можно меньше." +
            "Один из способов развивать стартап — делайте продукт, который нравится пользователям. Начните с чего-то простого и лёгкого, чем бы вы тоже пользовались. Выпустите версию 1.0 как можно скорее и продолжайте работать. Прислушивайтесь к пользователям, потому что клиент всегда прав. Но разные клиенты правы по-разному. Неопытные подскажут вам, что нужно упростить, а искушённые пользователи расскажут, какие функции стоит добавить." +
            "Стартапы проваливаются из-за того, что, во-первых, делают продукт, который никому не нужен, во-вторых, тратят слишком много времени и денег. Эти две причины убили так много стартапов, что я даже не уверен, существует ли третья. Так что если вы создаете что-то нужное и не слишком тратитесь на это — то вы на правильном пути." +
            "Когда вы впервые собираете деньги на проект, сначала спросите себя: «В нас стоит инвестировать?» Честный ответ на этот вопрос поможет и в следующий раз. Не рекламируйте. Говорите правду, и пусть инвесторы читают по глазам. Есть три условия успешного стартапа: работать с хорошими людьми, делать то, что нужно клиентам, и тратить как можно меньше." +
            "Один из способов развивать стартап — делайте продукт, который нравится пользователям. Начните с чего-то простого и лёгкого, чем бы вы тоже пользовались. Выпустите версию 1.0 как можно скорее и продолжайте работать. Прислушивайтесь к пользователям, потому что клиент всегда прав. Но разные клиенты правы по-разному. Неопытные подскажут вам, что нужно упростить, а искушённые пользователи расскажут, какие функции стоит добавить." +
            "Стартапы проваливаются из-за того, что, во-первых, делают продукт, который никому не нужен, во-вторых, тратят слишком много времени и денег. Эти две причины убили так много стартапов, что я даже не уверен, существует ли третья. Так что если вы создаете что-то нужное и не слишком тратитесь на это — то вы на правильном пути."

    val encryptedText = encryptText(text, keys[0])
    println("encryptedText: $encryptedText")

    val decryptedText = decryptText(encryptedText, keys[0])
    println("decryptedText: $decryptedText")

    val factors3 = kasiskiExamination(encryptedText, 3, 10)
    println("factors3: $factors3")
    val factors4 = kasiskiExamination(encryptedText, 4, 10)
    println("factors4: $factors4")
    val factors5 = kasiskiExamination(encryptedText, 5, 10)
    println("factors5: $factors5")

    val tmp = averageMatchesIndex(text, encryptedText, ABC.RUS)
    println("tmp: $tmp")

    val tmp1 = matchesIndex(text, encryptedText)
    println("tmp1: $tmp1")
}

fun kasiskiExamination(cipherText: String, sequenceLength: Int, maxKeyLength: Int): Map<Int, Int> {
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

    return factorCount.toMap()
}

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

fun isCorrectPermutation(permutation: List<Int>): Boolean {
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

fun allPermutations(list: List<Int>): List<List<Int>> {
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

fun matchesIndex(ySeq: String, zSeq: String): Double {
    assert(ySeq.length == zSeq.length)

    return ySeq.reduceIndexed {
            index, acc, ch -> acc + (if (ch == zSeq[index]) 1 else 0)
    }.toDouble() / ySeq.length
}

enum class ABC {
    ENG, RUS
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

fun vigenerEncrypt(text: String, key: List<Char>, abc:ABC=ABC.ENG): String {
    val actualABC = if (abc == ABC.ENG) englishABC else russianABC

    fun _vigenerEncrypt(_message: String, _key: List<Char>): String {
        var cipherText = ""
        _message.forEachIndexed { index, ch ->
            val kIdx = _key[index % _key.size]
            val cIdx = actualABC[(actualABC.indexOf(ch) + actualABC.indexOf(kIdx)) % actualABC.length]
            cipherText += cIdx
        }

        return cipherText
    }

    val words = text.split("\\s+".toRegex()).map { word ->
        word.replace("""^[,.]|[,.]$""".toRegex(), "")
    }

    return words.map { word -> _vigenerEncrypt(word, key) }.joinToString(" ")
}