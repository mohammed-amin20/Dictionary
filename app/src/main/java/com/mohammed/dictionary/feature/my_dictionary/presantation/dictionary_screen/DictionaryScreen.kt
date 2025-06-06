package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes.Empty
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes.MeaningItem
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes.NotFound


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    viewModel: DictionaryScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.uiAction.collect { uiAction ->
            when(uiAction){
                is UiAction.ShowToastMsg -> {
                    Toast.makeText(context, uiAction.msg , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
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
//               , navigationIcon = {
//                    Icon(
//                        painter = painterResource(R.drawable.dictionary),
//                        contentDescription = "logo"
//                    )
//                }
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
                    unfocusedContainerColor = Color(0xfffdf9f4)
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
            if (state.value.error== false && state.value.loading == false && state.value.word != null){
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
                            color = Color.Black
                        )
                        Text(
                            text = state.value.word!!.phonetic,//Phonetic
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    Color(0xff03575b).copy(.5f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .background(
                                    color = Color(0xff03575b).copy(.1f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    viewModel.onAction(
                                        DictionaryScreenAction.OnPlayAudioClicked(state.value.word?.audio!!)
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color(0xff03575b),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = state.value.word!!.phonetic,//Phonetic
                                fontSize = 16.sp,
                                color = Color.Black,
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
            if (state.value.loading){
                NotFound()
            }
            if(state.value.error){
                Empty()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PrevScreen() {
    DictionaryScreen()
}