package vk.cheysoff.keychain.presentation.screens.editpasswordscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import vk.cheysoff.keychain.domain.model.PasswordModel
import vk.cheysoff.keychain.domain.repository.KeychainRepository
import vk.cheysoff.keychain.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class EditPasswordScreenViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val repository: KeychainRepository
) : ViewModel() {
    var state by mutableStateOf(EditPasswordScreenState())
        private set

    fun processIntent(intent: EditPasswordScreenIntent) {
        when (intent) {
            EditPasswordScreenIntent.ClickSaveButtonIntent -> clickSaveButton()
            is EditPasswordScreenIntent.ChangeLoginFieldIntent -> changeLoginField(intent.text)
            is EditPasswordScreenIntent.ChangePasswordFieldIntent -> changePasswordField(intent.text)
            is EditPasswordScreenIntent.ChangeWebsiteUrlFieldIntent -> changeWebsiteUrl(intent.text)
            is EditPasswordScreenIntent.InitializeIntent -> initialize(intent.id)
            EditPasswordScreenIntent.ClickBackButtonIntent -> returnBack()
            EditPasswordScreenIntent.DeletePasswordIntent -> deletePasswordAction()
            is EditPasswordScreenIntent.ChangeNotesFieldIntent -> changeNotes(intent.text)
        }
    }

    private fun deletePasswordAction() {
        viewModelScope.launch {
            async { deletePassword() }.await()
            returnBack()
        }
    }

    private suspend fun deletePassword() {
        state.data?.let { model ->
            model.id?.let { repository.deletePassword(model.id) }
        }
    }

    private fun initialize(id: Long) {
        state = state.copy(
            loading = true
        )
        viewModelScope.launch {
            when (val result = repository.getPasswordById(id)) {
                is Resource.Success -> {
                    state = state.copy(
                        data = result.data,
                        loading = false,
                        websiteUrlFieldText = result.data?.websiteUrl ?: "",
                        loginFieldText = result.data?.login ?: "",
                        passwordFieldText = result.data?.password ?: "",
                        notesFieldText = result.data?.notes ?: ""
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        loading = false,
                        websiteUrlFieldText = "",
                        loginFieldText = "",
                        passwordFieldText = "",
                        notesFieldText = ""
                    )
                }
            }
        }
    }

    private fun returnBack() {
        state = state.copy(
            loadingChange = false,
            loading = false
        )
        navHostController.navigateUp()
    }

    private fun changeLoginField(text: String) {
        state = state.copy(
            loginFieldText = text
        )
    }

    private fun changePasswordField(text: String) {
        state = state.copy(
            passwordFieldText = text
        )
    }

    private fun changeWebsiteUrl(text: String) {
        state = state.copy(
            websiteUrlFieldText = text
        )
    }

    private fun changeNotes(text: String) {
        state = state.copy(
            notesFieldText = text
        )
    }

    private fun clickSaveButton() {
        state = state.copy(
            loadingChange = true,
        )
        viewModelScope.launch {
            async {
                when (val result = repository.insertPassword(
                    PasswordModel(
                        id = state.data?.id,
                        websiteUrl = ensureHttps(state.websiteUrlFieldText),
                        login = state.loginFieldText,
                        password = state.passwordFieldText,
                        notes = state.notesFieldText
                    )
                )) {
                    is Resource.Success -> state = state.copy(
                        loadingChange = false,
                        hasPasswordUpdated = true,
                    )

                    is Resource.Error -> state = state.copy(
                        loadingChange = false,
                        updateError = result.message
                    )
                }
            }.await()
            returnBack()
        }
    }

    private fun ensureHttps(url: String): String {
        val regex = Regex("^https?://")

        return if (!regex.containsMatchIn(url)) {
            "https://$url"
        } else {
            url
        }
    }
}