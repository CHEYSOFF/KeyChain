package vk.cheysoff.keychain.presentation.screens.editpasswordscreen

import vk.cheysoff.keychain.domain.model.PasswordModel

data class EditPasswordScreenState (
    var loading: Boolean = false,
    var loadingChange: Boolean = false,
    var hasPasswordUpdated: Boolean = false,
    var updateError: String? = null,
    var data: PasswordModel? = null,
    var websiteUrlFieldText: String = "",
    var loginFieldText: String = "",
    var passwordFieldText: String = "",
    var notesFieldText: String = "",
)