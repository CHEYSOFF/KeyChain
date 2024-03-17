package vk.cheysoff.keychain.presentation.screens.masterkeyscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vk.cheysoff.keychain.domain.model.AppDataModel
import vk.cheysoff.keychain.domain.repository.AppDataRepository
import vk.cheysoff.keychain.domain.util.Resource
import vk.cheysoff.keychain.presentation.NavigationItem
import java.lang.ref.WeakReference
import javax.inject.Inject


@HiltViewModel
class MasterKeyScreenViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val repository: AppDataRepository
) : ViewModel() {
    var state by mutableStateOf(MasterKeyScreenState())
        private set

    private lateinit var activityRef: WeakReference<FragmentActivity>

    fun processIntent(intent: MasterKeyScreenIntent) {
        when (intent) {
            is MasterKeyScreenIntent.InitializeIntent -> initialize(intent.fragmentActivity)
            is MasterKeyScreenIntent.ChangeFirstPasswordFieldIntent -> changeFirstPasswordField(
                intent.text
            )

            is MasterKeyScreenIntent.ChangeSecondPasswordFieldIntent -> changeSecondPasswordField(
                intent.text
            )

            MasterKeyScreenIntent.NextButtonClickIntent -> if (state.isFirstTime) {
                saveMasterKey()
            } else {
                attemptLogin()
            }

            MasterKeyScreenIntent.BiometricButtonClickIntent -> biometricButtonClick()
        }
    }

    private fun biometricButtonClick() {
        state = state.copy(
            loading = true
        )
        val curActivityRef = activityRef.get()
        if (curActivityRef == null) {
            state = state.copy(
                loading = false,
                loginError = "Something unexpected happened"
            )
        } else {
            repository.useBiometrics(fragmentActivity = curActivityRef,
                onSuccess = {
                    state = state.copy(
                        loading = false
                    )
                    navHostController.navigate(NavigationItem.PasswordsScreen.route)
                },
                onFailed = {
                    state = state.copy(
                        loginError = "Fingerprint or face id doesn't match",
                        loading = false
                    )
                },
                onError = { _, message ->
                    state = state.copy(
                        loginError = message.toString(),
                        loading = false
                    )
                })
        }
    }

    private fun changeFirstPasswordField(text: String) {
        state = state.copy(
            firstPasswordFieldText = text
        )
        checkPasswordMatching()
    }

    private fun changeSecondPasswordField(text: String) {
        state = state.copy(
            secondPasswordFieldText = text
        )
        checkPasswordMatching()
    }

    private fun checkPasswordMatching() {
        if (!state.isFirstTime) {
            state = state.copy(
                isButtonEnabled = true
            )
            return
        }
        val doPasswordMatch = state.firstPasswordFieldText == state.secondPasswordFieldText
        val arePasswordsBlank =
            state.firstPasswordFieldText.isBlank() || state.firstPasswordFieldText.isBlank()
        state = state.copy(
            loginError = if (!doPasswordMatch)
                "Passwords doesn't match"
            else if (arePasswordsBlank)
                "Passwords shouldn't be blank"
            else
                null,
            isButtonEnabled = doPasswordMatch && !arePasswordsBlank
        )
    }

    private fun initialize(fragmentActivity: FragmentActivity) {
        activityRef = WeakReference(fragmentActivity)
        state = state.copy(
            firstLoad = true,
            firstPasswordFieldText = "",
            secondPasswordFieldText = "",
            loading = false
        )
        viewModelScope.launch {
            when (val result = repository.getAppData()) {
                is Resource.Success -> {
                    state = if (result.data == null) {
                        state.copy(
                            isFirstTime = true,
                            isFingerPrintEnabled = false,
                            firstLoad = false
                        )
                    } else {
                        state.copy(
                            isFirstTime = false,
                            isFingerPrintEnabled = result.data.isBiometricEnabled,
                            firstLoad = false
                        )
                    }
                }

                is Resource.Error -> {
                    state = state.copy(
                        isFirstTime = true,
                        isFingerPrintEnabled = false,
                        firstLoad = false
                    )
                }
            }
        }

    }

    private fun saveMasterKey() {
        viewModelScope.launch {
            repository.upsertAppData(
                AppDataModel(
                    masterKey = state.firstPasswordFieldText,
                    isBiometricEnabled = state.isFingerPrintEnabled
                )
            )
            state = state.copy(
                loading = false
            )
            navHostController.navigate(NavigationItem.PasswordsScreen.route)
        }
    }

    private fun attemptLogin() {
        state = state.copy(
            loading = true
        )
        viewModelScope.launch {
            when (val result = repository.verifyPassword(state.firstPasswordFieldText)) {
                is Resource.Success -> {
                    if (result.data == true) {
                        state = state.copy(
                            loading = false
                        )
                        navHostController.navigate(NavigationItem.PasswordsScreen.route)
                    } else {
                        state = state.copy(
                            loginError = "Wrong password. Try again",
                            loading = false
                        )
                    }
                }

                is Resource.Error -> {
                    state = state.copy(
                        loading = false,
                        loginError = result.message
                    )
                }
            }
        }
        state = state.copy(
            loading = false
        )
    }

}