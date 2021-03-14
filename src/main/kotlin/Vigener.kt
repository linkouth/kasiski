class Vigener {
    companion object {
        fun vigenerEncrypt(text: String, key: List<Char>, abc:ABC=ABC.ENG): String {
            val actualABC = if (abc == ABC.ENG) englishABC else russianABC

            var cipherText = ""
            text.forEachIndexed { index, c ->
                val ch = c.toUpperCase()
                cipherText += if (actualABC.contains(ch)) {
                    val kIdx = key[index % key.size].toUpperCase()
                    val cIdx = actualABC[(actualABC.indexOf(ch) + actualABC.indexOf(kIdx)) % actualABC.length]
                    cIdx
                } else {
                    ch
                }
            }

            return cipherText
        }

        fun vigenerDecrypt(encryptedText: String, key: List<Char>, abc:ABC=ABC.ENG): String {
            val actualABC = if (abc == ABC.ENG) englishABC else russianABC
            var message = ""

            encryptedText.forEachIndexed { index, c ->
                val ch = c.toUpperCase()
                message += if (actualABC.contains(ch)) {
                    val kIdx = key[index % key.size].toUpperCase()
                    val mi = actualABC[(actualABC.indexOf(ch) - actualABC.indexOf(kIdx)).toBigInteger().mod(actualABC.length.toBigInteger()).toInt()]
                    mi
                } else {
                    ch
                }
            }

            return message
        }
    }
}