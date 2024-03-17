package vk.cheysoff.keychain.presentation.screens.settingsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import vk.cheysoff.keychain.R
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.BackButton
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.BasicTextField
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.FormattedGradientButton
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.ShowErrorText
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.TextFieldType

@Composable
fun ShowSettingsScreen(
    state: SettingsScreenState,
    onIntentReceived: (SettingsScreenIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            BackButton(
                onClick = { onIntentReceived(SettingsScreenIntent.BackButtonClickIntent) },
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable { onIntentReceived(SettingsScreenIntent.BiometricsButtonClickIntent) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.fingerprint),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                val text = if (state.biometricsUsed) {
                    "Biometric usage is enabled, click to disable"
                } else {
                    "Biometric usage is disabled, click to enable"
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change password",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                BasicTextField(text = state.oldPasswordTextField,
                    placeholderText = "Enter your old password",
                    textFieldType = TextFieldType.Password,
                    onValueChange = { text ->
                        onIntentReceived(SettingsScreenIntent.OldPasswordTextFieldChangeIntent(text))
                    })
                BasicTextField(text = state.newPasswordTextField,
                    placeholderText = "Enter your new password",
                    textFieldType = TextFieldType.Password,
                    onValueChange = { text ->
                        onIntentReceived(SettingsScreenIntent.NewPasswordTextFieldChangeIntent(text))
                    })
                FormattedGradientButton(
                    onClick = { onIntentReceived(SettingsScreenIntent.SaveButtonClickIntent) },
                    enabled = true,
                    buttonText = "Save"
                )
            }



            if (state.loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
            }

            if (state.showSuccessfulChange) {
                Text(
                    text = "Password changed successfully",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            state.error?.let { text ->
                ShowErrorText(text)
            }

        }
    }
}