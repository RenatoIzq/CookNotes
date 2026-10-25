package com.renato.cooknotes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PastelPinkPrimaryDark,
    secondary = PastelPeachSecondaryDark,
    tertiary = PastelLavenderTertiaryDark,
    background = PastelBackgroundDark,
    surface = PastelSurfaceDark,
    onPrimary = Color(0xFF3F2D2E),
    onSecondary = Color(0xFF432C05),
    onBackground = Color(0xFFECE0E1),
    onSurface = Color(0xFFECE0E1)
)

private val LightColorScheme = lightColorScheme(
    primary = PastelPinkPrimary,
    secondary = PastelPeachSecondary,
    tertiary = PastelLavenderTertiary,
    background = PastelBackground,
    surface = PastelSurface,
    surfaceVariant = PastelSurfaceVariant,
    onPrimary = PastelOnPrimary,
    onSecondary = PastelOnSecondary,
    onBackground = PastelOnBackground,
    onSurface = PastelOnSurface
)

@Composable
fun CookNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color can be disabled or optional; keeping dynamicColor = false by default to showcase our custom pastel theme!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
