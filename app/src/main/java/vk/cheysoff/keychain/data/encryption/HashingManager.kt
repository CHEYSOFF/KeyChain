package vk.cheysoff.keychain.data.encryption

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import javax.inject.Inject

class HashingManager @Inject constructor(private val argon2Kt: Argon2Kt) {

    fun slowHashStringWithSalt(passwordToHash: String, salt: String): String {
        return argon2Kt.hash(
            mode = Argon2Mode.ARGON2_I,
            password = passwordToHash.toByteArray(),
            salt = salt.toByteArray(),
            tCostInIterations = 5,
            mCostInKibibyte = 65536
        ).encodedOutputAsString()
    }

    fun verifyPassword(hashedPassword: String, otherPassword: String): Boolean {
        return argon2Kt.verify(
            mode = Argon2Mode.ARGON2_I,
            encoded = hashedPassword,
            password = otherPassword.toByteArray()
        )
    }

}
