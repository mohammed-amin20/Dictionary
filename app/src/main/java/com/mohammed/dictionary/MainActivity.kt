package com.mohammed.dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.DictionaryScreen
import com.mohammed.dictionary.ui.theme.DictionaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DictionaryTheme {
                DictionaryScreen()
            }
        }
    }
}

