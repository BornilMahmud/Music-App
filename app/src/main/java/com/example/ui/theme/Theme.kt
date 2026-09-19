package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Sonic Tangerine Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = SonicPrimaryContainer, // #FF7A00
    onPrimary = SonicWhite,
    primaryContainer = SonicPrimaryContainer,
    onPrimaryContainer = SonicOnPrimaryContainer,
    inversePrimary = SonicInversePrimary,
    secondary = SonicSecondary,
    onSecondary = SonicOnSecondary,
    secondaryContainer = SonicSecondaryContainer,
    onSecondaryContainer = SonicOnSecondaryContainer,
    tertiary = SonicTertiary,
    onTertiary = SonicOnTertiary,
    tertiaryContainer = SonicTertiaryContainer,
    onTertiaryContainer = SonicOnTertiaryContainer,
    background = SonicBackground, // #121317
    onBackground = SonicOnBackground, // #E3E2E7
    surface = SonicSurface, // #121317
    onSurface = SonicWhite,
    surfaceVariant = SonicSurfaceContainerHighest, // #343439
    onSurfaceVariant = SonicSlateMuted, // #8E92A0
    outline = SonicBorder, // #1F222C
    outlineVariant = SonicOutlineVariant,
    error = SonicError,
    onError = SonicOnError,
    errorContainer = SonicErrorContainer,
    onErrorContainer = SonicOnErrorContainer
)

// Sonic Tangerine Warm Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = SonicPrimaryContainer,
    onPrimary = Color.White,
    primaryContainer = SonicPrimaryFixed,
    onPrimaryContainer = SonicOnPrimaryFixed,
    secondary = SonicSecondaryContainer,
    onSecondary = Color.White,
    secondaryContainer = SonicSecondaryFixed,
    onSecondaryContainer = SonicOnSecondaryFixed,
    tertiary = SonicTertiaryContainer,
    onTertiary = Color.White,
    background = BmLightBackground,
    onBackground = BmLightTextPrimary,
    surface = BmLightSurface,
    onSurface = BmLightTextPrimary,
    surfaceVariant = BmLightSurfaceVariant,
    onSurfaceVariant = BmLightTextSecondary,
    outline = BmLightBorder,
    error = SonicError,
    onError = Color.White
)

@Composable
fun BMPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Sonic Tangerine default is atmospheric dark canvas
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = BMPlayerTheme(darkTheme = darkTheme, content = content)
