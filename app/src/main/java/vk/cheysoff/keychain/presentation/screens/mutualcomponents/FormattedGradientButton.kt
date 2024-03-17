package vk.cheysoff.keychain.presentation.screens.mutualcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vk.cheysoff.keychain.presentation.ui.theme.NextButtonActiveGradientList
import vk.cheysoff.keychain.presentation.ui.theme.NextButtonInActiveGradientList

@Composable
fun FormattedGradientButton(
    onClick: () -> Unit,
    enabled: Boolean,
    buttonText: String
) {
    GradientButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .height(40.dp),
        colors = if (enabled) NextButtonActiveGradientList else NextButtonInActiveGradientList
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GradientButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(42.dp)
            )
            .clip(RoundedCornerShape(42.dp))
            .background(
                brush = Brush.verticalGradient(colors = colors),
                shape = RoundedCornerShape(42.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        content = content
    )
}