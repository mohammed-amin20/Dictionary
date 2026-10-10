@file:OptIn(ExperimentalTextApi::class)

package com.mohammed.dictionary.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.mohammed.dictionary.R

/*
 * Bundled variable fonts. Weights are driven through the OpenType `wght` axis
 * so a single file covers the whole scale.
 */

private fun inter(weight: FontWeight) = Font(
    resId = R.font.inter,
    weight = weight,
    style = FontStyle.Normal,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

private fun arabicSans(weight: FontWeight) = Font(
    resId = R.font.noto_sans_arabic,
    weight = weight,
    style = FontStyle.Normal,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

private fun arabicDisplay(weight: FontWeight) = Font(
    resId = R.font.noto_naskh_arabic,
    weight = weight,
    style = FontStyle.Normal,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

/** Latin + Arabic body stack: Inter first, Noto Sans Arabic as the Arabic fallback. */
val AppFontFamily = FontFamily(
    inter(FontWeight.Normal),
    arabicSans(FontWeight.Normal),
    inter(FontWeight.Medium),
    arabicSans(FontWeight.Medium),
    inter(FontWeight.SemiBold),
    arabicSans(FontWeight.SemiBold),
    inter(FontWeight.Bold),
    arabicSans(FontWeight.Bold),
)

/** Display stack: Inter headings with elegant Noto Naskh for Arabic headings. */
val AppDisplayFontFamily = FontFamily(
    inter(FontWeight.Normal),
    arabicDisplay(FontWeight.Normal),
    inter(FontWeight.Medium),
    arabicDisplay(FontWeight.Medium),
    inter(FontWeight.SemiBold),
    arabicDisplay(FontWeight.SemiBold),
    inter(FontWeight.Bold),
    arabicDisplay(FontWeight.Bold),
)
