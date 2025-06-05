package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mohammed.dictionary.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen() {
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
                value = "",
                onValueChange = {

                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(0xfffdf9f4),
                    unfocusedContainerColor = Color(0xfffdf9f4)
                ),
                placeholder = {
                    Text(text = "Search", color = Color(0xff03575b).copy(.8f))
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
                    onSearch = {}
                )
            )
            Spacer(Modifier.height(height = 20.dp))
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
                        text = "eat",//word
                        fontWeight = FontWeight.W600,
                        fontSize = 40.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "/i:t/",//Phonetic
                        fontSize = 20.sp,
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
                            text = "/i:t/",//Phonetic
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
//                items() {
//
//                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PrevScreen() {
    DictionaryScreen()
}