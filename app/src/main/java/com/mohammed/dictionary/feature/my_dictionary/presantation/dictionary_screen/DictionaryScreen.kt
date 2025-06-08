package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohammed.dictionary.R
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes.Empty
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes.MeaningItem
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes.NotFound


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    viewModel: DictionaryScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.uiAction.collect { uiAction ->
            when (uiAction) {
                is UiAction.ShowToastMsg -> {
                    Toast.makeText(context, uiAction.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 8.dp),
                navigationIcon = {
                    Spacer(Modifier.width(16.dp))
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "Dictionary",
                        fontWeight = FontWeight.W700,
                        fontSize = 32.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff03575b),
                    titleContentColor = Color.White
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xff03575b))
                .padding(innerPadding)
        ) {
            TextField(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = state.value.search,
                onValueChange = {
                    viewModel.onAction(DictionaryScreenAction.OnSearchChanged(it))
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(0xfffdf9f4),
                    unfocusedContainerColor = Color(0xfffdf9f4),
                    cursorColor = Color(0xff03575b),
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    selectionColors = TextSelectionColors(
                        handleColor = Color(0xff03575b),
                        backgroundColor = Color(0xff03575b).copy(.2f)
                    )
                ),
                placeholder = {
                    Text(
                        text = "Search",
                        color = Color(0xff03575b).copy(.8f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color(0xff03575b).copy(.8f)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onAction(DictionaryScreenAction.OnImeSearch)
                    }
                )
            )
            Spacer(Modifier.height(height = 20.dp))

            if (!state.value.error && !state.value.loading && !state.value.noInternet && state.value.word != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(
                                topEnd = 24.dp,
                                topStart = 24.dp
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = state.value.word!!.word,//word
                            fontWeight = FontWeight.W600,
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = state.value.word!!.phonetic,//Phonetic
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        val animatedColor by animateColorAsState(
                            targetValue = if (state.value.playing) Color(0xff03575b).copy(alpha = 0.3f)
                            else Color(0xff03575b).copy(alpha = 0.1f),
                            animationSpec = tween(durationMillis = 500),
                            label = "PlayBackgroundColor"
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    Color(0xff03575b).copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .background(
                                    color = animatedColor,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    if (!state.value.playing)
                                        viewModel.onAction(
                                            DictionaryScreenAction.OnPlayAudioClicked(state.value.word?.audio!!)
                                        )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = if (state.value.playing)
                                    painterResource(R.drawable.ic_pause_24)
                                else
                                    painterResource(R.drawable.ic_play_arrow_24),
                                contentDescription = "Play/Pause",
                                tint = Color(0xff03575b),
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(Modifier.width(4.dp))

                            Text(
                                text = state.value.word!!.phonetic,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                    item {
                        state.value.word!!.meanings.forEach { meaning ->
                            MeaningItem(meaning)
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
            if (state.value.loading) {
                NotFound()
            }
            if (state.value.error) {
                Empty()
            }
            if (state.value.noInternet) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.no_internet2),
                        contentDescription = "No internet "
                    )
                }
            }
            if (!state.value.loading && !state.value.error && !state.value.noInternet && state.value.word == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.wordly_logo),
                        contentDescription = "Logo ",
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Start searching for definitions of some word",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    )

                }
            }

        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun PrevScreen() {
    DictionaryScreen()
}