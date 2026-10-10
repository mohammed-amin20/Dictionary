package com.mohammed.dictionary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/*
 * Extended, brand-specific tokens that the Material 3 color scheme does not
 * cover: gradients, glows, glass surfaces and card strokes. Access them with
 * `MaterialTheme.dictionaryColors` so screens stay decoupled from raw colors.
 */
@Immutable
data class DictionaryExtraColors(
    val backgroundGradient: List<Color>,
    val heroGradient: List<Color>,
    val goldGradient: List<Color>,
    val accentGlow: Color,
    val cardStroke: Color,
    val glassSurface: Color,
    val onGradient: Color,
)

val lightDictionaryColors = DictionaryExtraColors(
    backgroundGradient = listOf(BgTopLight, BgMidLight, BgBottomLight),
    heroGradient = listOf(HeroTopLight, HeroBottomLight),
    goldGradient = listOf(GoldTopLight, GoldBottomLight),
    accentGlow = AccentGlowLight,
    cardStroke = CardStrokeLight,
    glassSurface = GlassSurfaceLight,
    onGradient = Color(0xFFFFFFFF),
)

val darkDictionaryColors = DictionaryExtraColors(
    backgroundGradient = listOf(BgTopDark, BgMidDark, BgBottomDark),
    heroGradient = listOf(HeroTopDark, HeroBottomDark),
    goldGradient = listOf(GoldTopDark, GoldBottomDark),
    accentGlow = AccentGlowDark,
    cardStroke = CardStrokeDark,
    glassSurface = GlassSurfaceDark,
    onGradient = Color(0xFFEAF7F6),
)

val LocalDictionaryColors = staticCompositionLocalOf { lightDictionaryColors }

val MaterialTheme.dictionaryColors: DictionaryExtraColors
    @Composable
    @ReadOnlyComposable
    get() = LocalDictionaryColors.current
