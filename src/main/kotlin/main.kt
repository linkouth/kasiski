import java.io.File

fun main(args: Array<String>) {
    val arguments = parseArguments(args)

    when (arguments[MODE]) {
        "1" -> firstTask(arguments)
        "2" -> secondTask(arguments)
    }
}

fun parseArguments(args: Array<String>): Map<String, String> {
    return args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), elem ->
        if (elem.startsWith("-"))  Pair(map + (elem to emptyList()), elem)
        else Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem), lastKey)
    }.first.mapValues { it.value.first() }
}

fun firstTask(arguments: Map<String, String>) {
    when (arguments[STEP]) {
        "1" -> {
            val keyLength = arguments[KEY_LENGTH]?.toInt() ?: 5
            val key = Permutations.getKey(keyLength)
            File("$FIRST_TASK_OUTPUT/$KEY_PATH").writeText("$key")
        }
        "2" -> {
            val text = File("$FIRST_TASK_OUTPUT/$OPEN_MESSAGE").readText()
            val key = File("$FIRST_TASK_OUTPUT/$KEY_PATH").readText()
                .replace("[", "").replace("]", "")
                .split(", ")
                .map { it.toInt() }
            val cipherText = Permutations.encryptText(text, key)

            File("$FIRST_TASK_OUTPUT/$ENCRYPTED_TEXT").writeText(cipherText)
        }
        "3" -> {
            val encryptedText = File("$FIRST_TASK_OUTPUT/$ENCRYPTED_TEXT").readText()
            val key = File("$FIRST_TASK_OUTPUT/$KEY_PATH").readText()
                .replace("[", "").replace("]", "")
                .split(", ")
                .map { it.toInt() }
            val decryptedText = Permutations.decryptText(encryptedText, key)

            File("$FIRST_TASK_OUTPUT/$DECRYPTED_TEXT").writeText(decryptedText)
        }
        "4" -> {
            val text = File("$FIRST_TASK_OUTPUT/$OPEN_MESSAGE").readText()
            val key = File("$FIRST_TASK_OUTPUT/$VIGENER_KEY_PATH").readText()
                .split(", ")
                .map { it[0] }
            val cipherText = Vigener.vigenerEncrypt(text, key, abc = ABC.RUS)

            File("$FIRST_TASK_OUTPUT/$VIGENER_ENCRYPTED_TEXT").writeText(cipherText)
        }
        "5" -> {
            val encryptedText = File("$FIRST_TASK_OUTPUT/$VIGENER_ENCRYPTED_TEXT").readText()
            val key = File("$FIRST_TASK_OUTPUT/$VIGENER_KEY_PATH").readText()
                .split(", ")
                .map { it[0] }
            val decryptedText = Vigener.vigenerDecrypt(encryptedText, key, abc = ABC.RUS)

            File("$FIRST_TASK_OUTPUT/$VIGENER_DECRYPTED_TEXT").writeText(decryptedText)
        }
        "6" -> {
            val nGramLength = arguments[KEY_LENGTH]?.toInt() ?: 3
            val encryptedText = File("$FIRST_TASK_OUTPUT/$ENCRYPTED_TEXT").readText()
            val (gcd, factors) = Kasiski.examination(encryptedText, nGramLength, 10)
            println("Тест Казиски для $nGramLength-грам: $gcd")
            println("$factors")
        }
        "7" -> {
            val nGramLength = arguments[KEY_LENGTH]?.toInt() ?: 4
            val encryptedText = File("$FIRST_TASK_OUTPUT/$VIGENER_ENCRYPTED_TEXT").readText()
            val (gcd, factors) = Kasiski.examination(encryptedText, nGramLength, 10)
            println("Тест Казиски для $nGramLength-грам: $gcd")
            println("$factors")
        }
    }
}

fun secondTask(arguments: Map<String, String>) {
}
