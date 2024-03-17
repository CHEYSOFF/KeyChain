package vk.cheysoff.keychain.presentation.screens.mutualcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import vk.cheysoff.keychain.R


@Composable
fun BasicTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholderText: String,
    textFieldType: TextFieldType,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        value = text,

        onValueChange = { newText ->
            onValueChange(newText)
        },
        singleLine = singleLine,

        placeholder = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),

        textStyle = MaterialTheme.typography.bodySmall
            .copy(color = MaterialTheme.colorScheme.primary),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),

        shape = RoundedCornerShape(12.dp),

        visualTransformation = when (textFieldType) {
            TextFieldType.Password ->
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(
                    '\u25CF'
                )

            TextFieldType.Basic -> VisualTransformation.None
        },

        trailingIcon = {
            when (textFieldType) {
                TextFieldType.Password -> {
                    val image = if (passwordVisible) R.drawable.show_password_icon
                    else R.drawable.dont_show_password_icon

                    val description =
                        if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = description,
                            tint = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                }

                TextFieldType.Basic -> {}
            }
        }
    )


}