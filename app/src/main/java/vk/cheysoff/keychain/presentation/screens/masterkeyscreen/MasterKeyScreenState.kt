package vk.cheysoff.keychain.presentation.screens.masterkeyscreen

data class MasterKeyScreenState (
    val isFirstTime: Boolean = true,
    val isFingerPrintEnabled: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val firstPasswordFieldText: String = "",
    val secondPasswordFieldText: String = "",
    val loginError: String? = null,
    val loading: Boolean = false,
    val firstLoad: Boolean = false,
)