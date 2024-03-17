package vk.cheysoff.keychain.presentation.screens.editpasswordscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.BackButton
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.BasicTextField
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.FormattedGradientButton
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.ShowErrorText
import vk.cheysoff.keychain.presentation.screens.mutualcomponents.TextFieldType

@Composable
fun EditPasswordScreen(
    state: EditPasswordScreenState,
    onIntentReceived: (EditPasswordScreenIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseSurface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .align(Alignment.Center)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                BackButton(
                    onClick = { onIntentReceived(EditPasswordScreenIntent.ClickBackButtonIntent) },
                    modifier =
                    Modifier.align(Alignment.CenterStart)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BasicTextField(
                    text = state.websiteUrlFieldText,
                    placeholderText = "Enter website url",
                    textFieldType = TextFieldType.Basic,
                    onValueChange = { text ->
                        onIntentReceived(
                            EditPasswordScreenIntent.ChangeWebsiteUrlFieldIntent(
                                text
                            )
                        )
                    },
                )
                BasicTextField(
                    text = state.loginFieldText,
                    placeholderText = "Enter your login",
                    textFieldType = TextFieldType.Basic,
                    onValueChange = { text ->
                        onIntentReceived(EditPasswordScreenIntent.ChangeLoginFieldIntent(text))
                    },
                )
                BasicTextField(
                    text = state.passwordFieldText,
                    placeholderText = "Enter your password",
                    textFieldType = TextFieldType.Password,
                    onValueChange = { text ->
                        onIntentReceived(EditPasswordScreenIntent.ChangePasswordFieldIntent(text))
                    },
                )
                BasicTextField(
                    text = state.notesFieldText,
                    placeholderText = "Enter your notes",
                    textFieldType = TextFieldType.Basic,
                    onValueChange = { text ->
                        onIntentReceived(EditPasswordScreenIntent.ChangeNotesFieldIntent(text))
                    },
                    singleLine = false
                )
                FormattedGradientButton(
                    onClick = { onIntentReceived(EditPasswordScreenIntent.ClickSaveButtonIntent) },
                    enabled = true,
                    buttonText = "Save"
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(40.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(42.dp)
                        ),
                    onClick = { onIntentReceived(EditPasswordScreenIntent.DeletePasswordIntent) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(42.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }

                if (state.loadingChange) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.inverseSurface,
                    )
                }

                state.updateError?.let { text ->
                    ShowErrorText(text)
                }

            }
        }
    }
}