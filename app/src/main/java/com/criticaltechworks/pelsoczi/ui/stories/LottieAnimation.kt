package com.criticaltechworks.pelsoczi.ui.stories

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun LottieComposition(
    @RawRes rawResId: Int,
    modifier: Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawResId))
    LottieAnimation(
        composition = composition,
        modifier = modifier,
    )
}