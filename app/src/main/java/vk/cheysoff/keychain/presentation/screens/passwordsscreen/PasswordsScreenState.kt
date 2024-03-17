package vk.cheysoff.keychain.presentation.screens.passwordsscreen

import vk.cheysoff.keychain.domain.model.PasswordModel

data class PasswordsScreenState (
    var data: List<PasswordModel> = listOf(),
    var loading: Boolean = false,
    var isSearching: Boolean = false,
    var error: String? = null,
    val searchText: String = "",
)