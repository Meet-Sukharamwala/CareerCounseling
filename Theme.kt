package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BoldPurpleLight,
    onPrimary = BoldPurpleDark,
    primaryContainer = BoldPurpleDark,
    onPrimaryContainer = BoldPurpleLight,
    secondary = BoldLilacAccent,
    onSecondary = BoldDarkInk,
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = BoldPurpleContainer,
    tertiary = BoldPinkAccent,
    onTertiary = Color(0xFF492532),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF49454F)
)

private val LightColorScheme = lightColorScheme(
    primary = BoldPurple,
    onPrimary = Color.White,
    primaryContainer = BoldPurpleLight,
    onPrimaryContainer = BoldPurpleDark,
    secondary = Color(0xFF625B71),
    onSecondary = Color.White,
    secondaryContainer = BoldPurpleContainer,
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color.White,
    tertiaryContainer = BoldPinkAccent,
    onTertiaryContainer = Color(0xFF31111D),
    background = BoldBackground,
    onBackground = BoldDarkInk,
    surface = BoldSurface,
    onSurface = BoldDarkInk,
    surfaceVariant = BoldSurfaceVariant,
    onSurfaceVariant = BoldMediumNeutral,
    outline = BoldOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted academic theme for consistency
    content: @Composable () -> Unit,
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

