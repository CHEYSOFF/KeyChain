package vk.cheysoff.keychain.data.mappers

import vk.cheysoff.keychain.data.encryption.CryptoManager
import vk.cheysoff.keychain.data.encryption.HashingManager
import vk.cheysoff.keychain.data.local.entities.AppDataEntity
import vk.cheysoff.keychain.data.local.entities.PasswordEntity
import vk.cheysoff.keychain.domain.model.AppDataModel
import vk.cheysoff.keychain.domain.model.PasswordModel

fun AppDataEntity.toAppDataModel(): AppDataModel {
    return AppDataModel(
        masterKey = hashedMasterKey,
        isBiometricEnabled = isBiometricEnabled
    )
}

fun AppDataModel.toHashedAppDataEntity(
    hashingManager: HashingManager,
    salt: String
): AppDataEntity {
    return AppDataEntity(
        hashedMasterKey = masterKey.toHashedPassword(hashingManager, salt),
        isBiometricEnabled = isBiometricEnabled
    )
}

fun PasswordEntity.toDecryptedPasswordModel(
    cryptoManager: CryptoManager,
    password: String
): PasswordModel {
    return PasswordModel(
        id = id,
        websiteUrl = websiteUrl,
        login = cryptoManager.decrypt(loginCyphered, password),
        password = cryptoManager.decrypt(passwordCyphered, password),
        notes = notes
    )
}

fun PasswordModel.toEncryptedPasswordEntity(
    cryptoManager: CryptoManager,
    password: String
): PasswordEntity {
    return PasswordEntity(
        id = id,
        websiteUrl = websiteUrl,
        loginCyphered = cryptoManager.encrypt(this.login, password),
        passwordCyphered = cryptoManager.encrypt(this.password, password),
        notes = notes
    )
}

fun String.toHashedPassword(
    hashingManager: HashingManager,
    salt: String
): String {
    return hashingManager.slowHashStringWithSalt(this, salt)
}