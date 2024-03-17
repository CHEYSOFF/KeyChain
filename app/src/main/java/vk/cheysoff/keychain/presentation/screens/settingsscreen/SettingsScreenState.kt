package vk.cheysoff.keychain.presentation.screens.settingsscreen

data class SettingsScreenState (
    var loading: Boolean = false,
    var biometricsUsed: Boolean = false,
    var error: String? = null,
    var oldPasswordTextField: String = "",
    var newPasswordTextField: String = "",
    var showSuccessfulChange: Boolean = false,
    var isButtonActive: Boolean = false
)