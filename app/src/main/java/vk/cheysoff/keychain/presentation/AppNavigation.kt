package vk.cheysoff.keychain.presentation

enum class Screen {
    WelcomeScreen,
    PasswordsScreen,
    PasswordInfoScreen,
    SettingsScreen
}

sealed class NavigationItem(val route: String) {
    data object WelcomeScreen : NavigationItem(Screen.WelcomeScreen.name)
    data object PasswordsScreen : NavigationItem(Screen.PasswordsScreen.name)
    data object PasswordInfoScreen : NavigationItem(Screen.PasswordInfoScreen.name)
    data object SettingsScreen : NavigationItem(Screen.SettingsScreen.name)
}