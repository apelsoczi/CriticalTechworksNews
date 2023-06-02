package com.criticaltechworks.pelsoczi.ui.detail

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.criticaltechworks.pelsoczi.data.model.Headline
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse
import com.criticaltechworks.pelsoczi.ui.theme.CriticalTechworksNewsTheme
import com.criticaltechworks.pelsoczi.ui.theme.Typography
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController,
) {
    val viewState = viewModel.viewState.collectAsStateWithLifecycle()
    when (val headline = viewState.value.headline) {
        null -> navController.popBackStack()
        else -> DetailScreen(
            headline = headline,
            landscape = LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE,
        )
    }
}

@Composable
private fun DetailScreen(
    headline: Headline,
    landscape: Boolean,
) {
    if (landscape.not()) {
        PortraitDetail(headline = headline)
    } else {
        LandscapeDetail(headline = headline)
    }
}

@Composable
private fun PortraitDetail(
    headline: Headline
) {
    Column(Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(bottom = 4.dp),
            model = headline.cachedImageKey,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            headline.author.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    style = Typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
            }
            Text(
                text = DateTimeFormatter.ofPattern("MMMM d, H:mm a")
                    .withZone(ZoneId.systemDefault())
                    .format(headline.published),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                style = Typography.labelLarge,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 96.dp),
                color = Color.White,
            )
            Text(
                text = headline.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = Typography.headlineSmall,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }
        headline.description.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White),
                style = Typography.bodyLarge,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
        headline.content.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = Typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun LandscapeDetail(
    headline: Headline
) {
    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = headline.cachedImageKey,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(0.7F))
            ) {
                headline.author.takeIf { it.isNotEmpty() }?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                    )
                }
                Text(
                    text = DateTimeFormatter.ofPattern("MMMM d, H:mm a")
                        .withZone(ZoneId.systemDefault())
                        .format(headline.published),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    style = Typography.labelMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 96.dp),
                    color = Color.White,
                )
                Text(
                    text = headline.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    style = Typography.headlineSmall,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
            }
            Column(
                Modifier.background(Color.White.copy(0.7F))
                    .fillMaxHeight()
            ) {
                headline.description.takeIf { it.isNotEmpty() }?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
                        style = Typography.bodyLarge,
                    )
                }
                headline.content.takeIf { it.isNotEmpty() }?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        style = Typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Preview(heightDp = 891, widthDp = 411)
@Composable
private fun HeadlineDetailPortraitPreview() {
    Surface {
        CriticalTechworksNewsTheme {
            DetailScreen(
                headline = headlineOne,
                landscape = false
            )
        }
    }
}

@Preview(heightDp = 411, widthDp = 891)
@Composable
fun HeadlineDetailLandscapePreview() {
    Surface {
        CriticalTechworksNewsTheme {
            DetailScreen(
                headline = headlineOne,
                landscape = true,
            )
        }
    }
}

private val sourceBbc = TopStoriesResponse.ApiResponse.Article.Source(
    id = "bbc-news",
    name = "BBC News",
)
private val firstArticle = TopStoriesResponse.ApiResponse.Article(
    source = sourceBbc,
    author = "BBC News",
    title = "China's C919 passenger plane makes maiden flight",
    description = "China hopes the C919 will end the dominance of Airbus and Boeing - but it relies on Western components.",
    url = "http://www.bbc.co.uk/news/world-asia-china-65737081",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/126F5/production/_129890557_0c36b96325745e2c5ab3d9927a594cbd5d7c18e7-1.jpg",
    publishedAt = Instant.parse("2001-01-01T15:52:23.652744300Z"),
    content = "China's first domestically produced passenger jet has taken off on its maiden commercial flight. ",
)
private val headlineOne = Headline(
    firstArticle,
    "https://ichef.bbci.co.uk/news/1024/branded_news/126F5/production/_129890557_0c36b96325745e2c5ab3d9927a594cbd5d7c18e7-1.jpg",
)