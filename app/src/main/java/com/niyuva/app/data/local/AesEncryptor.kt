package com.niyuva.app.data.local

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesEncryptor {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_LENGTH_BYTE = 12

    fun encrypt(data: ByteArray, keyString: String): ByteArray {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keyBytes = getKeyBytes(keyString)
        val secretKey = SecretKeySpec(keyBytes, "AES")
        val iv = ByteArray(IV_LENGTH_BYTE)
        SecureRandom().nextBytes(iv)
        val spec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
        val encryptedData = cipher.doFinal(data)
        
        // Output IV + Encrypted Data
        return iv + encryptedData
    }

    fun decrypt(encryptedDataWithIv: ByteArray, keyString: String): ByteArray {
        val keyBytes = getKeyBytes(keyString)
        val secretKey = SecretKeySpec(keyBytes, "AES")
        val iv = encryptedDataWithIv.copyOfRange(0, IV_LENGTH_BYTE)
        val encryptedData = encryptedDataWithIv.copyOfRange(IV_LENGTH_BYTE, encryptedDataWithIv.size)
        
        val cipher = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        return cipher.doFinal(encryptedData)
    }

    private fun getKeyBytes(keyString: String): ByteArray {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        return digest.digest(keyString.toByteArray(Charsets.UTF_8))
    }
}
