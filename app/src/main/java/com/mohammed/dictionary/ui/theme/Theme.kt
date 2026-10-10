package com.mohammed.dictionary.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealInk,
    onPrimary = OnTealInk,
    primaryContainer = TealInkContainer,
    onPrimaryContainer = OnTealInkContainer,
    secondary = TealMuted,
    onSecondary = OnTealMuted,
    secondaryContainer = TealMutedContainer,
    onSecondaryContainer = OnTealMutedContainer,
    tertiary = Gold,
    onTertiary = OnGold,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = OnGoldContainer,
    background = Paper,
    onBackground = OnPaper,
    surface = PaperSurface,
    onSurface = OnPaperSurface,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    scrim = Color(0xFF000000)
)

private val DarkColors = darkColorScheme(
    primary = TealInkDark,
    onPrimary = OnTealInkDark,
    primaryContainer = TealInkContainerDark,
    onPrimaryContainer = OnTealInkContainerDark,
    secondary = TealMutedDark,
    onSecondary = OnTealMutedDark,
    secondaryContainer = TealMutedContainerDark,
    onSecondaryContainer = OnTealMutedContainerDark,
    tertiary = GoldDark,
    onTertiary = OnGoldDark,
    tertiaryContainer = GoldContainerDark,
    onTertiaryContainer = OnGoldContainerDark,
    background = InkBackgroundDark,
    onBackground = OnInkBackgroundDark,
    surface = InkSurfaceDark,
    onSurface = OnInkSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    scrim = Color(0xFF000000)
)

@Composable
fun DictionaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
