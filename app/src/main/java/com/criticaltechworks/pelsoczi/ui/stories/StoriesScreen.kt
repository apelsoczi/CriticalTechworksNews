package com.criticaltechworks.pelsoczi.ui.stories

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.criticaltechworks.pelsoczi.R
import com.criticaltechworks.pelsoczi.data.model.Headline
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article.Source
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewIntent.RefreshStories
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewState.*
import com.criticaltechworks.pelsoczi.ui.theme.CriticalTechworksNewsTheme
import com.criticaltechworks.pelsoczi.ui.theme.Typography
import com.criticaltechworks.pelsoczi.util.RememberSaveableEffect
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun StoriesScreen(
    viewModel: StoriesViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    RememberSaveableEffect {
        viewModel.handle(RefreshStories)
    }
    StoriesScreen(
        loading = viewState.loading,
        headlines = viewState.headlines,
        contentError = viewState.noContent,
        internetError = viewState.internetError,
        landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE,
        handle = viewModel::handle
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StoriesScreen(
    loading: Boolean,
    headlines: List<Headline>,
    contentError: Boolean,
    internetError: Boolean,
    landscape: Boolean,
    handle: (action: StoriesViewIntent) -> Unit,
) {
    when {
        contentError -> {
            PromptScreen(
                titleResId = R.string.no_content_title,
                messageResId = R.string.no_content_message,
                lottieResId = R.raw.content_error,
                landscape = landscape,
                handle = handle,
            )
        }
        headlines.isNotEmpty() -> {
            if (landscape.not()) {
                LazyColumn(Modifier.fillMaxHeight()) {
                    items(headlines) { headline ->
                        PortraitHeadline(headline)
                    }
                }
            } else {
                val viewPort = object : PageSize {
                    override fun Density.calculateMainAxisPageSize(
                        availableSpace: Int,
                        pageSpacing: Int
                    ): Int {
                        return (availableSpace - 2 * pageSpacing) / 1
                    }
                }
                VerticalPager(
                    pageCount = headlines.size,
                    pageSize = viewPort,
                ) { index ->
                    HeadlinesLandscape(headlines[index])
                }
            }
        }
        internetError -> {
            PromptScreen(
                titleResId = R.string.no_internet_title,
                messageResId = R.string.no_internet_message,
                lottieResId = R.raw.internet_required,
                landscape = landscape,
                handle = handle,
            )
        }
    }
    LoadingSpinner(loading)
}



@Composable
fun LoadingSpinner(
    loading: Boolean
) {
    if (loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun PortraitHeadline(
    headline: Headline,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RectangleShape,
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = headline.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = Typography.titleMedium,
            )
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(bottom = 4.dp),
                model = headline.cachedImageKey,
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
            )
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = headline.author,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    style = Typography.labelMedium,
                )
                Spacer(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                )
                Text(
                    text = DateTimeFormatter.ofPattern("MMMM d, H:mm a")
                        .withZone(ZoneId.systemDefault())
                        .format(headline.published),
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    style = Typography.labelMedium,
                )
            }
            Text(
                text = headline.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                style = Typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun HeadlinesLandscape(
    headline: Headline
) {
    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = headline.cachedImageKey,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.White.copy(alpha = 0.7F))
        ) {
            Text(
                text = headline.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                style = Typography.titleLarge,
                maxLines = 2,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        if (headline.author.isNotEmpty()) {
                            withStyle(
                                Typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    .toSpanStyle()
                            ) {
                                append(headline.author)
                            }
                            withStyle(Typography.bodyMedium.toSpanStyle()) {
                                append(" on ")
                            }
                        }
                        withStyle(
                            Typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                .toSpanStyle()
                        ) {
                            append(
                                DateTimeFormatter.ofPattern("MMMM d, H:mm a")
                                    .withZone(ZoneId.systemDefault())
                                    .format(headline.published)
                            )
                            append(": ")
                        }
                        withStyle(Typography.bodyMedium.toSpanStyle()) {
                            append(headline.description)
                        }
                    },
                    style = Typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@Preview(heightDp = 891, widthDp = 411)
@Composable
fun HeadlinesPortraitPreview() {
    Surface {
        CriticalTechworksNewsTheme {
            StoriesScreen(
                loading = false,
                headlines = listOf(headlineOne, headlineTwo, headlineThree),
                contentError = false,
                internetError = false,
                landscape = false,
                handle = {}
            )
        }
    }
}

@Preview(heightDp = 411, widthDp = 891)
@Composable
fun HeadlinesLandscapePreview() {
    Surface {
        CriticalTechworksNewsTheme {
            StoriesScreen(
                loading = false,
                headlines = listOf(headlineOne, headlineTwo, headlineThree),
                contentError = false,
                internetError = false,
                landscape = false,
                handle = {}
            )
        }
    }
}

private val source = Source(
    id = "bbc-news",
    name = "BBC News",
)
private val articleOne = Article(
    source = source,
    author = "BBC News",
    title = "Erdogan hails election victory but Turkey left divided",
    description = "The president's supporters celebrate long into the night after he secures five more years in power.",
    url = "http://www.bbc.co.uk/news/world-europe-65743031",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/3119/production/_129896521_6f24438a3e65ba472cc4a1a120756c4d8d597c520_314_5500_30961000x563.jpg",
    publishedAt = Instant.parse("2023-05-29T00:22:14.6028734Z"),
    content = "Recep Tayyip Erdogan's supporters celebrated well into the night after Turkey's long-time president secured another five years in power.",
)
private val headlineOne = Headline(
    articleOne,
    "",
)

private val articleTwo = Article(
    source = source,
    author = "BBC News",
    title = "US debt ceiling deal ready for Congress vote - President Joe Biden",
    description = "President Joe Biden said he had not made too many concessions to Republicans as part of the deal.",
    url = "http://www.bbc.co.uk/news/world-us-canada-65741462",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/11A11/production/_129890227_0ccb4097535e127765b701e471ae75d5ca3ce71e0_65_1536_8641000x563.jpg",
    publishedAt = Instant.parse("2023-05-28T22:52:15.6961467Z"),
    content = "US President Joe Biden and Republican House Speaker Kevin McCarthy have reached a bipartisan deal to raise the US debt ceiling and avert a default, President Biden has said.",
)
private val headlineTwo = Headline(
    articleTwo,
    "",
)

private val articleThree = Article(
    source = source,
    author = "BBC News",
    title = "Lake Maggiore tourist boat carrying 20 overturns, with one dead",
    description = "Italy's fire service said 19 people were rescued but it was still searching for some people reported to be missing.",
    url = "http://www.bbc.co.uk/news/world-europe-65741465",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/83B3/production/_115651733_breaking-large-promo-nc.png",
    publishedAt = Instant.parse("2023-05-28T21:07:12.5113529Z"),
    content = "A boat carrying at least 20 people has overturned on Lake Maggiore in northern Italy.\\r\\nItalian media is reporting that one person has died and at least three others remain missing.\\r\\nItaly's fire serv…",
)
private val headlineThree = Headline(
    articleThree,
    "",
)

private val articleNulls = Article(
    source = source,
    author = "",
    title = "Lake Maggiore tourist boat carrying 20 overturns, with one dead",
    description = "",
    url = "http://www.bbc.co.uk/news/world-europe-65741465",
    urlToImage = "",
    publishedAt = Instant.parse("2023-05-28T21:07:12.5113529Z"),
    content = "",
)
private val headlineNulls = Headline(
    articleNulls,
    "",
)