package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mohammed.dictionary.R
import com.mohammed.dictionary.ui.theme.Sizes
import com.mohammed.dictionary.ui.theme.Spacing

@Composable
fun LoadingSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.45f)
                .height(40.dp)
                .shimmer(MaterialTheme.shapes.large)
        )
        Box(
            Modifier
                .fillMaxWidth(0.25f)
                .height(18.dp)
                .shimmer(MaterialTheme.shapes.small)
        )
        Spacer(Modifier.height(Spacing.sm))
        repeat(3) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .shimmer(MaterialTheme.shapes.small)
            )
        }
        repeat(2) {
            Spacer(Modifier.height(Spacing.sm))
            Box(
                Modifier
                    .fillMaxWidth(0.5f)
                    .height(18.dp)
                    .shimmer(MaterialTheme.shapes.small)
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .shimmer(MaterialTheme.shapes.small)
            )
        }
    }
}

@Composable
fun IdleState(
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.dictionary_background),
            contentDescription = stringResource(R.string.illustration_content_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(Sizes.illustrationLarge)
        )
        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = stringResource(R.string.idle_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.xs))
        Text(
            text = stringResource(R.string.idle_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.xl))
        SuggestedWords(onSuggestionClick = onSuggestionClick)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestedWords(
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val words = stringArrayResource(R.array.suggested_words)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.try_these),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(Spacing.sm))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            words.forEach { word ->
                SuggestionChip(
                    onClick = { onSuggestionClick(word) },
                    label = { Text(word) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    border = null
                )
            }
        }
    }
}

@Composable
fun NotFoundState(modifier: Modifier = Modifier) {
    StateMessage(
        title = stringResource(R.string.not_found_title),
        modifier = modifier
    )
}

@Composable
fun ErrorState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    StateMessage(
        title = stringResource(R.string.error_title),
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
fun ServerErrorState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    StateMessage(
        title = stringResource(R.string.server_error_title),
        subtitle = stringResource(R.string.server_error_subtitle),
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
fun NoInternetState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.no_internet),
            contentDescription = stringResource(R.string.illustration_content_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(Sizes.illustrationLarge)
        )
        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = stringResource(R.string.no_internet_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.xs))
        Text(
            text = stringResource(R.string.no_internet_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.xl))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
private fun StateMessage(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )
        Spacer(Modifier.height(Spacing.lg))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        if (subtitle != null) {
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(Spacing.xl))
            Button(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}
