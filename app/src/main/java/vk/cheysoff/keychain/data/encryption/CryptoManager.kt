package vk.cheysoff.keychain.data.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun encryptCipher(password: CharArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey(password))
        }
    }

    private fun getDecryptCipherForIv(iv: ByteArray, password: CharArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(password), IvParameterSpec(iv))
        }
    }

    private fun getKey(password: CharArray): SecretKey {
        val protParam: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(password)
        val existingKey = keyStore.getEntry("secret", protParam) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(data: String, password: String): ByteArray {
        return encrypt(data.toByteArray(), password.toCharArray())
    }

    fun encrypt(bytes: ByteArray, password: CharArray): ByteArray {
        val cipher = encryptCipher(password)
        val encryptedBytes = cipher.doFinal(bytes)
        val iv = cipher.iv

        val ivSize = iv.size.toByte()
        val encryptedBytesSize = encryptedBytes.size.toByte()

        return byteArrayOf(ivSize) + iv + byteArrayOf(encryptedBytesSize) + encryptedBytes
    }

    fun decrypt(data: ByteArray, password: String): String {
        return String(decrypt(data, password.toCharArray()))
    }

    fun decrypt(bytes: ByteArray, password: CharArray): ByteArray {

        val ivSize = bytes[0].toInt()
        val iv = bytes.copyOfRange(1, ivSize + 1)

        val encryptedBytesSize = bytes[ivSize + 1].toInt()
        val encryptedBytes = bytes.copyOfRange(ivSize + 2, ivSize + 2 + encryptedBytesSize)

        return getDecryptCipherForIv(iv, password).doFinal(encryptedBytes)

    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}