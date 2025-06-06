package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning

@Composable
fun MeaningItem(meaning: Meaning) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "* ${meaning.partOfSpeech}",
            fontSize = 24.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))
        meaning.definitions.forEach { definition ->
            Column {
                Text(
                    text = "- ${definition.definition}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                if (definition.example.isNotEmpty()){
                    Row{
                        Text(
                            text = "Example :",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = definition.example.trim(),
                            fontStyle = FontStyle.Italic,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}