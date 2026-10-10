package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mohammed.dictionary.R
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word
import com.mohammed.dictionary.ui.theme.Spacing
import com.mohammed.dictionary.ui.theme.dictionaryColors

@Composable
fun WordHeader(
    word: Word,
    modifier: Modifier = Modifier
) {
    val extras = MaterialTheme.dictionaryColors
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(Brush.linearGradient(extras.heroGradient))
                .padding(horizontal = Spacing.md, vertical = Spacing.xxs)
        ) {
            Text(
                text = stringResource(R.string.english_label),
                style = MaterialTheme.typography.labelMedium,
                color = extras.onGradient
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Text(
            text = word.word,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(Spacing.md))
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(Brush.linearGradient(extras.goldGradient))
        )
    }
}
