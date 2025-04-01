package ru.itport.sportinventory.utils


import ru.itport.sportinventory.utils.HashUtils.sha256b
import ru.itport.sportinventory.models.auth.TelegramAuthRq
import ru.itport.sportinventory.models.auth.forceSerialize

class TelegramVerifier {

    fun checkTelegramData(payload: TelegramAuthRq, botToken: String): Boolean {
        val serializedString = payload.forceSerialize("\n", sorted = true)
            .split("\n").filter { !it.startsWith("hash") && !it.contains("null") }.joinToString("\n")
        return HashUtils.hex(HashUtils.hmac_sha256(serializedString, botToken.sha256b())) == payload.hash
    }

}