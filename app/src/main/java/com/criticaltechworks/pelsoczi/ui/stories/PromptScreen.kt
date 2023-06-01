package com.criticaltechworks.pelsoczi.ui.stories

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.criticaltechworks.pelsoczi.R
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewIntent.RefreshStories
import com.criticaltechworks.pelsoczi.ui.theme.CriticalTechworksNewsTheme
import com.criticaltechworks.pelsoczi.ui.theme.Typography

@Composable
fun PromptScreen(
    @StringRes titleResId: Int,
    @StringRes messageResId: Int,
    @RawRes lottieResId: Int,
    landscape: Boolean,
    handle: (action: StoriesViewIntent) -> Unit
) {
    if (landscape) {
        Row(Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp)) {
            LottieComposition(
                rawResId = lottieResId,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1F),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F),
            ) {
                Column {
                    Text(
                        text = stringResource(id = titleResId),
                        style = Typography.headlineMedium,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(id = messageResId),
                        style = Typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
                Button(
                    onClick = { handle(RefreshStories) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, end = 48.dp)
                        .align(Alignment.BottomCenter),
                ) {
                    Text(
                        text = stringResource(id = R.string.try_again),
                    )
                }
            }
        }
    }
    else {
        Box(Modifier.fillMaxSize().padding(32.dp)) {
            Column {
                Text(
                    text = stringResource(id = titleResId),
                    style = Typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                )
                LottieComposition(
                    rawResId = lottieResId,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                )
                Text(
                    text = stringResource(id = messageResId),
                    style = Typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 32.dp),
                )
            }
            Button(
                onClick = { handle(RefreshStories) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 48.dp, bottom = 32.dp)
                    .align(Alignment.BottomCenter),
            ) {
                Text(
                    text = stringResource(id = R.string.try_again),
                )
            }
        }
    }
}

@Preview(heightDp = 891, widthDp = 411)
@Composable
fun PromptScreenPortrait() {
    Surface {
        CriticalTechworksNewsTheme {
            PromptScreen(
                titleResId = R.string.no_internet_title,
                messageResId = R.string.no_internet_message,
                lottieResId = R.raw.internet_required,
                landscape = false,
                handle = {},
            )
        }
    }
}

@Preview(heightDp = 411, widthDp = 891)
@Composable
fun PromptScreenLandscape() {
    Surface {
        CriticalTechworksNewsTheme {
            PromptScreen(
                titleResId = R.string.no_content_title,
                messageResId = R.string.no_content_message,
                lottieResId = R.raw.content_error,
                landscape = true,
                handle = {},
            )
        }
    }
}