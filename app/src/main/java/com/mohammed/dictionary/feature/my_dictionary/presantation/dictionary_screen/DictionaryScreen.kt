package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohammed.dictionary.R
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.DictionarySearchBar
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.IdleState
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.LoadingSkeleton
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.MeaningCard
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.NoInternetState
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.NotFoundState
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.PronunciationButton
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.ServerErrorState
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.TranslationCard
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components.WordHeader
import com.mohammed.dictionary.ui.theme.Sizes
import com.mohammed.dictionary.ui.theme.Spacing
import com.mohammed.dictionary.ui.theme.dictionaryColors

private enum class ContentState { Idle, Loading, NoInternet, Server, NotFound, Content }

private fun DictionaryScreenState.toContentState(): ContentState = when {
    loading -> ContentState.Loading
    noInternet -> ContentState.NoInternet
    serverError -> ContentState.Server
    error -> ContentState.NotFound
    word != null -> ContentState.Content
    else -> ContentState.Idle
}

@Composable
fun DictionaryScreen(
    viewModel: DictionaryScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiAction.collect { action ->
            val message = when (action) {
                UiAction.EmptyQuery -> context.getString(R.string.enter_word)
                UiAction.NoAudio -> context.getString(R.string.no_audio)
                UiAction.SoundError -> context.getString(R.string.sound_error)
                is UiAction.Remote -> action.text
            }
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AmbientBackground()
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { DictionaryHeader() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DictionarySearchBar(
                    value = state.search,
                    loading = state.loading,
                    onValueChange = { viewModel.onAction(DictionaryScreenAction.OnSearchChanged(it)) },
                    onSearch = { viewModel.onAction(DictionaryScreenAction.OnImeSearch) },
                    modifier = Modifier.padding(
                        start = Spacing.lg,
                        end = Spacing.lg,
                        top = Spacing.md,
                        bottom = Spacing.sm
                    )
                )

                AnimatedContent(
                    targetState = state.toContentState(),
                    transitionSpec = {
                        (
                            fadeIn(animationSpec = tween(durationMillis = 300)) +
                                scaleIn(
                                    initialScale = 0.97f,
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            ) togetherWith fadeOut(animationSpec = tween(durationMillis = 160))
                    },
                    label = "dictionaryContent",
                    modifier = Modifier.fillMaxSize()
                ) { contentState ->
                    when (contentState) {
                        ContentState.Loading -> LoadingSkeleton()

                        ContentState.NoInternet -> NoInternetState(
                            onRetry = { viewModel.onAction(DictionaryScreenAction.OnImeSearch) }
                        )

                        ContentState.Server -> ServerErrorState(
                            onRetry = { viewModel.onAction(DictionaryScreenAction.OnImeSearch) }
                        )

                        ContentState.NotFound -> NotFoundState()

                        ContentState.Idle -> IdleState(
                            onSuggestionClick = { word ->
                                viewModel.onAction(DictionaryScreenAction.OnSearchChanged(word))
                                viewModel.onAction(DictionaryScreenAction.OnImeSearch)
                            }
                        )

                        ContentState.Content -> WordContent(
                            state = state,
                            onPlayAudio = { text, audioUrl ->
                                viewModel.onAction(
                                    DictionaryScreenAction.OnPlayAudioClicked(text, audioUrl)
                                )
                            },
                            onTranslate = { viewModel.onAction(DictionaryScreenAction.OnTranslate) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AmbientBackground() {
    val extras = MaterialTheme.dictionaryColors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(brush = Brush.verticalGradient(extras.backgroundGradient))

                val glow = extras.accentGlow
                val topCenter = Offset(size.width * 0.85f, -size.height * 0.05f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(glow, Color.Transparent),
                        center = topCenter,
                        radius = size.minDimension * 0.9f
                    ),
                    radius = size.minDimension * 0.9f,
                    center = topCenter
                )

                val bottomCenter = Offset(size.width * 0.1f, size.height * 1.02f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(glow, Color.Transparent),
                        center = bottomCenter,
                        radius = size.minDimension * 0.85f
                    ),
                    radius = size.minDimension * 0.85f,
                    center = bottomCenter
                )
            }
    )
}

@Composable
private fun WordContent(
    state: DictionaryScreenState,
    onPlayAudio: (String, String?) -> Unit,
    onTranslate: () -> Unit
) {
    val word: Word = state.word ?: return
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Spacing.xl,
            end = Spacing.xl,
            top = Spacing.sm,
            bottom = Spacing.xxl
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        item { WordHeader(word = word) }
        item {
            PronunciationButton(
                phonetic = word.phonetic,
                playing = state.playing,
                enabled = word.word.isNotBlank(),
                onClick = { onPlayAudio(word.word, word.audio) }
            )
        }
        item {
            TranslationCard(
                translation = state.translation,
                translating = state.translating,
                error = state.translationError,
                onRetry = onTranslate
            )
        }
        items(word.meanings) { meaning ->
            MeaningCard(meaning = meaning)
        }
    }
}

@Composable
private fun DictionaryHeader() {
    val extras = MaterialTheme.dictionaryColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Sizes.logoBadge)
                .clip(MaterialTheme.shapes.small)
                .background(extras.glassSurface)
                .border(1.dp, extras.cardStroke, MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_content_description),
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.width(Spacing.md))
        Column {
            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.brand_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
