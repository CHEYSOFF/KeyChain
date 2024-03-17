package vk.cheysoff.keychain.presentation.screens.masterkeyscreen

import androidx.fragment.app.FragmentActivity

sealed class MasterKeyScreenIntent {
    data class InitializeIntent(val fragmentActivity: FragmentActivity): MasterKeyScreenIntent()
    class ChangeFirstPasswordFieldIntent(val text: String): MasterKeyScreenIntent()
    class ChangeSecondPasswordFieldIntent(val text: String): MasterKeyScreenIntent()
    data object NextButtonClickIntent: MasterKeyScreenIntent()
    data object BiometricButtonClickIntent: MasterKeyScreenIntent()
}

