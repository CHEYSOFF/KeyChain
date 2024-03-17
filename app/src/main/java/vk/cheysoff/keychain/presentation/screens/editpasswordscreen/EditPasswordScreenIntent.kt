package vk.cheysoff.keychain.presentation.screens.editpasswordscreen

sealed class EditPasswordScreenIntent {
    data object ClickSaveButtonIntent : EditPasswordScreenIntent()
    data object ClickBackButtonIntent : EditPasswordScreenIntent()
    data object DeletePasswordIntent : EditPasswordScreenIntent()
    data class InitializeIntent(val id: Long) : EditPasswordScreenIntent()
    data class ChangeWebsiteUrlFieldIntent(val text: String) : EditPasswordScreenIntent()
    data class ChangeLoginFieldIntent(val text: String) : EditPasswordScreenIntent()
    data class ChangePasswordFieldIntent(val text: String) : EditPasswordScreenIntent()
    data class ChangeNotesFieldIntent(val text: String) : EditPasswordScreenIntent()
}