package vk.cheysoff.keychain.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = Black,
    primaryContainer = HardGray,
    secondaryContainer = TransparentPink20,
    primary = Violet,
    inversePrimary = Black,
    surface = Black,
    secondary = MidGray,
    inverseSurface = Violet,
    surfaceTint = Black,
    error = Peach,
    errorContainer = Red,
    tertiary = LightGreen
)

private val LightColorScheme = lightColorScheme(
    background = White,
    primaryContainer = GrayishWhite,
    secondaryContainer = TransparentPink20,
    primary = DarkPurple,
    inversePrimary = White,
    surface = White,
    inverseSurface = DarkPurple,
    surfaceTint = White,
    error = Peach,
    errorContainer = Red,
    tertiary = LightGreen

)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if(darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}