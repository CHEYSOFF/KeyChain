package vk.cheysoff.keychain.presentation.screens.passwordsscreen

sealed class PasswordsScreenIntent {
    data object InitializeIntent: PasswordsScreenIntent()
    class OpenPasswordInfoIntent(val id: Long?): PasswordsScreenIntent()
    data object OpenSettingsIntent: PasswordsScreenIntent()
    class ChangeSearchBarTextIntent(val text: String): PasswordsScreenIntent()
    data object SearchIntent: PasswordsScreenIntent()
    data object OnToggleSearchIntent: PasswordsScreenIntent()
    data object ClearSearchBarIntent: PasswordsScreenIntent()
}