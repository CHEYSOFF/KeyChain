package vk.cheysoff.keychain.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import vk.cheysoff.keychain.presentation.screens.editpasswordscreen.EditPasswordScreen
import vk.cheysoff.keychain.presentation.screens.editpasswordscreen.EditPasswordScreenIntent
import vk.cheysoff.keychain.presentation.screens.editpasswordscreen.EditPasswordScreenViewModel
import vk.cheysoff.keychain.presentation.screens.masterkeyscreen.MasterKeyScreenIntent
import vk.cheysoff.keychain.presentation.screens.masterkeyscreen.MasterKeyScreenViewModel
import vk.cheysoff.keychain.presentation.screens.masterkeyscreen.ShowMasterKeyScreen
import vk.cheysoff.keychain.presentation.screens.passwordsscreen.PasswordsScreenIntent
import vk.cheysoff.keychain.presentation.screens.passwordsscreen.PasswordsScreenViewModel
import vk.cheysoff.keychain.presentation.screens.passwordsscreen.ShowPasswordsScreen
import vk.cheysoff.keychain.presentation.screens.settingsscreen.SettingsScreenIntent
import vk.cheysoff.keychain.presentation.screens.settingsscreen.SettingsScreenViewModel
import vk.cheysoff.keychain.presentation.screens.settingsscreen.ShowSettingsScreen
import vk.cheysoff.keychain.presentation.ui.theme.MyApplicationTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val masterKeyScreenViewModel: MasterKeyScreenViewModel by viewModels()
    private val passwordsScreenViewModel: PasswordsScreenViewModel by viewModels()
    private val editPasswordScreenViewModel: EditPasswordScreenViewModel by viewModels()
    private val settingsScreenViewModel: SettingsScreenViewModel by viewModels()

    @Inject
    lateinit var navHostController: NavHostController

    private val PASSWORD_ID = "id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {

                NavHost(
                    navController = navHostController,
                    startDestination = NavigationItem.WelcomeScreen.route
                ) {

                    composable(NavigationItem.WelcomeScreen.route) {
                        LaunchedEffect(Unit) {
                            masterKeyScreenViewModel.processIntent(
                                MasterKeyScreenIntent.InitializeIntent(
                                    this@MainActivity
                                )
                            )
                        }

                        ShowMasterKeyScreen(
                            masterKeyScreenViewModel.state
                        ) { intent ->
                            masterKeyScreenViewModel.processIntent(intent)
                        }
                    }

                    composable(
                        NavigationItem.PasswordsScreen.route,
                    ) {

                        LaunchedEffect(Unit) {
                            passwordsScreenViewModel.processIntent(PasswordsScreenIntent.InitializeIntent)
                        }
                        ShowPasswordsScreen(state = passwordsScreenViewModel.state) { intent ->
                            passwordsScreenViewModel.processIntent(intent)
                        }
                    }

                    composable(
                        NavigationItem.SettingsScreen.route,
                    ) {

                        LaunchedEffect(Unit) {
                            settingsScreenViewModel.processIntent(SettingsScreenIntent.InitializeIntent)
                        }
                        ShowSettingsScreen(state = settingsScreenViewModel.state) { intent ->
                            settingsScreenViewModel.processIntent(intent)
                        }
                    }

                    composable(
                        route = NavigationItem.PasswordInfoScreen.route + "/{$PASSWORD_ID}",
                        arguments = listOf(
                            navArgument(name = PASSWORD_ID) {
                                type = NavType.LongType
                                defaultValue = 0
                            }
                        )
                    ) { backstackEntry ->

                        LaunchedEffect(Unit) {
                            val passwordId = backstackEntry.arguments?.getLong(PASSWORD_ID) ?: 0
                            editPasswordScreenViewModel.processIntent(
                                EditPasswordScreenIntent.InitializeIntent(passwordId)
                            )
                        }

                        EditPasswordScreen(
                            state = editPasswordScreenViewModel.state,
                            onIntentReceived = { intent ->
                                editPasswordScreenViewModel.processIntent(intent)
                            }
                        )
                    }

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("CHEYSAFSA", "fsa")
        navHostController.popBackStack(
            route = NavigationItem.WelcomeScreen.route,
            inclusive = false,
            saveState = false
        )
    }
}
