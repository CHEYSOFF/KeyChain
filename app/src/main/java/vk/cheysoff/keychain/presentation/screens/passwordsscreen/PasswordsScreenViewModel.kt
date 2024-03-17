package vk.cheysoff.keychain.presentation.screens.passwordsscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vk.cheysoff.keychain.domain.repository.KeychainRepository
import vk.cheysoff.keychain.domain.util.Resource
import vk.cheysoff.keychain.presentation.NavigationItem
import javax.inject.Inject


@HiltViewModel
class PasswordsScreenViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val repository: KeychainRepository
) : ViewModel() {

    var state by mutableStateOf(PasswordsScreenState())
        private set

    fun processIntent(intent: PasswordsScreenIntent) {
        when (intent) {
            PasswordsScreenIntent.InitializeIntent -> initialize()
            is PasswordsScreenIntent.OpenPasswordInfoIntent -> openPasswordInfo(intent.id)
            is PasswordsScreenIntent.ChangeSearchBarTextIntent -> onSearchTextChange(intent.text)
            PasswordsScreenIntent.OnToggleSearchIntent -> onToggleSearch()
            PasswordsScreenIntent.SearchIntent -> searchPasswords()
            PasswordsScreenIntent.OpenSettingsIntent -> openSettings()
            PasswordsScreenIntent.ClearSearchBarIntent -> onSearchTextChange("")
        }
    }

    private fun onSearchTextChange(text: String) {
        state = state.copy(searchText = text)
        searchPasswords()
    }

    private fun onToggleSearch() {
        state = state.copy(isSearching = !state.isSearching)
        if (!state.isSearching) {
            onSearchTextChange("")
        }
    }

    private fun initialize() {
        state = state.copy(
            loading = true
        )
        viewModelScope.launch {
            state = when (val result = repository.getPasswords()) {
                is Resource.Success -> {
                    state.copy(
                        data = result.data ?: listOf(),
                        loading = false
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

    private fun searchPasswords() {
        state = state.copy(
            isSearching = true,
            error = null
        )
        viewModelScope.launch {
            state = when (val result = repository.searchPasswords(state.searchText)) {
                is Resource.Success -> {
                    state.copy(
                        data = result.data ?: listOf(),
                        isSearching = false
                    )
                }

                is Resource.Error -> {
                    state.copy(
                        isSearching = false,
                        error = result.message
                    )
                }
            }
        }
    }

    private fun openPasswordInfo(id: Long?) {
        navHostController.navigate(NavigationItem.PasswordInfoScreen.route + "/${id ?: -1}")
    }

    private fun openSettings() {
        navHostController.navigate(NavigationItem.SettingsScreen.route)
    }

}