package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mohammed.dictionary.R

@Composable
fun NotFound() {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                NotFoundAnimation()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Please wait while we get the definition...",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
}

@Composable
fun NotFoundAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.not_found))
    LottieAnimation(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .semantics { contentDescription = "Loading Animation" },
        iterations = LottieConstants.IterateForever,
        composition = composition,
    )
}