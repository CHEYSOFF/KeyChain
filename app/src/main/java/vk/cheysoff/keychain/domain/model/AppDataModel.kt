package vk.cheysoff.keychain.domain.model

data class AppDataModel (
    val masterKey: String,
    val isBiometricEnabled: Boolean
)