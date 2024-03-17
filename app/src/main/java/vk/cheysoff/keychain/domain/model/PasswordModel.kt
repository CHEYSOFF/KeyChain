package vk.cheysoff.keychain.domain.model

data class PasswordModel(
    val id: Long?,
    val websiteUrl: String,
    val login: String,
    val password: String,
    val notes: String
)