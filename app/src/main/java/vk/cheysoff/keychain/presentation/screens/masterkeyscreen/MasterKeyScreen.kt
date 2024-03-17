package vk.cheysoff.keychain.presentation.screens.masterkeyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vk.cheysoff.keychain.R
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.BasicTextField
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.FormattedGradientButton
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.ShowErrorText
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.TextFieldType

@Composable
fun ShowMasterKeyScreen(
    state: MasterKeyScreenState,
    onIntentReceived: (MasterKeyScreenIntent) -> Unit,
) {
    val hintText =
        if (state.isFirstTime) {
            "To continue create master key to the app and remember it\n\n" +
                    "You'll need it everytime you sign in\n\n" +
                    "(alternatively you can use fingerprint later)"
        } else {
            "Enter your master key" +
                    if (state.isFingerPrintEnabled) "\n\nor enter using fingerprint"
                    else ""
        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (state.firstLoad) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxHeight(0.35f)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = hintText,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Box(modifier = Modifier.fillMaxHeight()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ShowFirstField(
                            onIntentReceived = onIntentReceived,
                            text = state.firstPasswordFieldText
                        )

                        if (state.isFirstTime) {
                            ShowSecondField(
                                onIntentReceived = onIntentReceived,
                                text = state.secondPasswordFieldText
                            )
                        }

                        ShowNextButton(
                            onClick = { onIntentReceived(MasterKeyScreenIntent.NextButtonClickIntent) },
                            isEnabled = state.isButtonEnabled
                        )

                        if (state.isFingerPrintEnabled) {

                            ShowFingerPrintButton { onIntentReceived(MasterKeyScreenIntent.BiometricButtonClickIntent) }

                        }

                        state.loginError?.let { text ->
                            Spacer(modifier = Modifier.height(12.dp))
                            ShowErrorText(text)
                        }
                        if (state.loading) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                        }

                    }
                }
            }

        }
    }
}


@Composable
private fun ShowFirstField(onIntentReceived: (MasterKeyScreenIntent) -> Unit, text: String) {
    BasicTextField(
        text = text,
        placeholderText = "Enter your master password",
        textFieldType = TextFieldType.Password,
        onValueChange = { newText ->
            onIntentReceived(
                MasterKeyScreenIntent.ChangeFirstPasswordFieldIntent(
                    newText
                )
            )
        }
    )
}

@Composable
private fun ShowSecondField(onIntentReceived: (MasterKeyScreenIntent) -> Unit, text: String) {
    BasicTextField(
        text = text,
        placeholderText = "Enter your master password again",
        textFieldType = TextFieldType.Password,
        onValueChange = { newText ->
            onIntentReceived(
                MasterKeyScreenIntent.ChangeSecondPasswordFieldIntent(
                    newText
                )
            )
        }
    )
}

@Composable
private fun ShowNextButton(onClick: () -> Unit, isEnabled: Boolean) {
    FormattedGradientButton(
        onClick = onClick,
        enabled = isEnabled,
        buttonText = "Enter"
    )
}

@Composable
private fun ShowFingerPrintButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.size(70.dp),
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.fingerprint),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.background
        )
    }
}


