package vk.cheysoff.keychain.presentation.screens.settingsscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import vk.cheysoff.keychain.domain.repository.AppDataRepository
import vk.cheysoff.keychain.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val repository: AppDataRepository
) : ViewModel() {


    var state by mutableStateOf(SettingsScreenState())
        private set

    fun processIntent(intent: SettingsScreenIntent) {
        when (intent) {
            SettingsScreenIntent.BackButtonClickIntent -> clickBackButton()
            SettingsScreenIntent.BiometricsButtonClickIntent -> clickBiometricsButton()
            SettingsScreenIntent.InitializeIntent -> initialize()
            is SettingsScreenIntent.OldPasswordTextFieldChangeIntent -> changeOldPasswordField(
                intent.text
            )

            is SettingsScreenIntent.NewPasswordTextFieldChangeIntent -> changeNewPasswordField(
                intent.text
            )

            SettingsScreenIntent.SaveButtonClickIntent -> changePassword()
        }
    }

    private fun changeOldPasswordField(text: String) {
        state = state.copy(
            oldPasswordTextField = text
        )
    }

    private fun changeNewPasswordField(text: String) {
        state = state.copy(
            newPasswordTextField = text,
            isButtonActive = text.isNotBlank(),
            error = if (text.isBlank()) "New password shouldn't be empty" else ""
        )
    }

    private fun clickBackButton() {
        navHostController.navigateUp()
    }

    private fun initialize() {
        state = state.copy(
            loading = true
        )
        viewModelScope.launch {
            state = when (val result = repository.getAppData()) {
                is Resource.Success -> {
                    state.copy(
                        loading = false,
                        biometricsUsed = result.data?.isBiometricEnabled ?: false
                    )
                }

                is Resource.Error -> {
                    state.copy(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    private fun clickBiometricsButton() {
        state = state.copy(
            loading = true,
            biometricsUsed = !state.biometricsUsed
        )
        viewModelScope.launch {
            async { repository.updateBiometricUsage(state.biometricsUsed) }.await()
            state = state.copy(
                loading = false
            )
        }
    }

    private fun changePassword() {
        state = state.copy(
            loading = true,
            error = null,
            showSuccessfulChange = false,
        )
        viewModelScope.launch {
            when (val result = repository.verifyPassword(state.oldPasswordTextField)) {
                is Resource.Success -> {
                    state = if (result.data == true) {
                        async { repository.updateMasterKeyHash(state.newPasswordTextField) }.await()
                        state.copy(
                            showSuccessfulChange = true,
                        )
                    } else {
                        state.copy(
                            error = "You entered wrong old password"
                        )
                    }
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = "You entered wrong old password"
                    )
                }
            }

        }

        viewModelScope.launch {
            async { repository.updateBiometricUsage(state.biometricsUsed) }.await()
            state = state.copy(
                loading = false
            )
        }
    }
}