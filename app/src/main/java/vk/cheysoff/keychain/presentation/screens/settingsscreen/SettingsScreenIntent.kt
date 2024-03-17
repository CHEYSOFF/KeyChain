package vk.cheysoff.keychain.presentation.screens.settingsscreen

sealed class SettingsScreenIntent {
    data object BackButtonClickIntent: SettingsScreenIntent()
    data object SaveButtonClickIntent: SettingsScreenIntent()
    data object InitializeIntent: SettingsScreenIntent()
    data object BiometricsButtonClickIntent: SettingsScreenIntent()
    data class OldPasswordTextFieldChangeIntent(val text: String): SettingsScreenIntent()
    data class NewPasswordTextFieldChangeIntent(val text: String): SettingsScreenIntent()
}