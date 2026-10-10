package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohammed.dictionary.R
import com.mohammed.dictionary.ui.theme.Elevation
import com.mohammed.dictionary.ui.theme.Spacing
import com.mohammed.dictionary.ui.theme.dictionaryColors

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

/*
 * Shared, fully theme-aware illustration: a glowing gradient disc with a soft
 * pulsing halo. Replaces the old static PNGs so dark mode looks intentional.
 */
@Composable
fun StateIllustration(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    gradient: List<Color> = MaterialTheme.dictionaryColors.heroGradient,
    iconTint: Color = MaterialTheme.dictionaryColors.onGradient,
    discSize: Dp = 164.dp,
) {
    val extras = MaterialTheme.dictionaryColors
    val transition = rememberInfiniteTransition(label = "stateIllustration")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "stateIllustrationPulse"
    )

    Box(
        modifier = modifier.size(discSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(discSize * (0.74f + pulse * 0.16f))
                .clip(CircleShape)
                .background(extras.accentGlow)
        )
        Box(
            modifier = Modifier
                .size(discSize * 0.64f)
                .shadow(
                    elevation = Elevation.hero,
                    shape = CircleShape,
                    ambientColor = extras.accentGlow,
                    spotColor = extras.accentGlow
                )
                .clip(CircleShape)
                .background(Brush.linearGradient(gradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(discSize * 0.28f)
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
        StateIllustration(icon = Icons.Rounded.AutoStories)
        Spacer(Modifier.height(Spacing.xl))
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
        Spacer(Modifier.height(Spacing.xxl))
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
                SuggestionChipItem(word = word, onClick = { onSuggestionClick(word) })
            }
        }
    }
}

@Composable
private fun SuggestionChipItem(
    word: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = word,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.sm)
        )
    }
}

@Composable
fun NotFoundState(modifier: Modifier = Modifier) {
    StateMessage(
        icon = Icons.Rounded.SearchOff,
        title = stringResource(R.string.not_found_title),
        modifier = modifier
    )
}

@Composable
fun ErrorState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    StateMessage(
        icon = Icons.Rounded.CloudOff,
        title = stringResource(R.string.error_title),
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
fun ServerErrorState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    StateMessage(
        icon = Icons.Rounded.CloudOff,
        title = stringResource(R.string.server_error_title),
        subtitle = stringResource(R.string.server_error_subtitle),
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
fun NoInternetState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    StateMessage(
        icon = Icons.Rounded.WifiOff,
        title = stringResource(R.string.no_internet_title),
        subtitle = stringResource(R.string.no_internet_subtitle),
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
private fun StateMessage(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StateIllustration(icon = icon)
        Spacer(Modifier.height(Spacing.xl))
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
            Spacer(Modifier.height(Spacing.xxl))
            Button(
                onClick = onAction,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                )
            }
        }
    }
}
