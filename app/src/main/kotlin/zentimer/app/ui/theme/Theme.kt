package zentimer.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ZenDarkColorScheme = darkColorScheme(
    primary = ZenPrimary,
    onPrimary = Color.Black,
    secondary = ZenAccent,
    onSecondary = Color.White,
    background = ZenBackground,
    onBackground = ZenText,
    surface = ZenSurface,
    onSurface = ZenText,
    surfaceVariant = ZenSurface,
    onSurfaceVariant = ZenMuted,
    outline = ZenMuted
)

@Composable
fun ZenTimerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ZenDarkColorScheme,
        typography = ZenTypography,
        content = content
    )
}
