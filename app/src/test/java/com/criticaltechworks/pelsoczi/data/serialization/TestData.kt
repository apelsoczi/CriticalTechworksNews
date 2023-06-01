package com.criticaltechworks.pelsoczi.data.serialization

import com.criticaltechworks.pelsoczi.data.model.Headline
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse
import com.criticaltechworks.pelsoczi.util.encode
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray
import java.time.Instant


// region: error json

val errorJson = buildJsonObject {
    put("status", encode("error"))
    put("code", encode("apiKeyMissing"))
    put("message", encode("Your API key is missing."))
}

// endregion

// region: complete json

val sourceJson = buildJsonObject {
    put("id", encode("bbc-news"))
    put("name", encode("BBC News"))
}
val articleJsonFirst = buildJsonObject {
    put("source", sourceJson)
    put("author", encode("BBC News"))
    put("title", encode("China's C919 passenger plane makes maiden flight"))
    put("description", encode("China hopes the C919 will end the dominance of Airbus and Boeing - but it relies on Western components."))
    put("url", encode("http://www.bbc.co.uk/news/world-asia-china-65737081"))
    put("urlToImage", encode("https://ichef.bbci.co.uk/news/1024/branded_news/126F5/production/_129890557_0c36b96325745e2c5ab3d9927a594cbd5d7c18e7-1.jpg"))
    put("publishedAt", encode("2001-01-01T15:52:23.652744300Z"))
    put("content", encode("China's first domestically produced passenger jet has taken off on its maiden commercial flight. \\r\\nState TV showed the C919 rising into the skies above Shanghai, heading to the capital Beijing early … [+960 chars]"))
}
val articleJsonSecond = buildJsonObject {
    put("source", sourceJson)
    put("author", encode("BBC News"))
    put("title", encode("Will US debt ceiling deal pass? Biden and McCarthy must still walk tightrope"))
    put("description", encode("Both leaders have work to do to convince their parties this is a deal to back when Congress votes."))
    put("url", encode("http://www.bbc.co.uk/news/world-us-canada-65759537"))
    put("urlToImage", encode("https://ichef.bbci.co.uk/news/1024/branded_news/2F6F/production/_129934121_capitol2_976getty.jpg"))
    put("publishedAt", encode("2002-02-02T15:52:23.652744300Z"))
    put("content", encode("When it comes to raising the US debt ceiling and avoiding a national default, reaching a deal between the two parties was an uphill battle. But there are more steep hills to climb.\nNow the two leade… [+3283 chars]"))
}
val articleJsonThird = buildJsonObject {
    put("source", sourceJson)
    put("author", encode("BBC News"))
    put("title", encode("Nvidia hits 1tn valuation as investors buy AI buzz"))
    put("description", encode("Investors excited about the possibilities for artificial intelligence have sent the US chipmaker's value soaring."))
    put("url", encode("http://www.bbc.co.uk/news/business-65757812"))
    put("urlToImage", encode("https://ichef.bbci.co.uk/news/1024/branded_news/9941/production/_129933293_gettyimages-1258278958.jpg"))
    put("publishedAt", encode("2003-03-03T15:37:22.5896408Z"))
    put("content", encode("The elite club of US companies worth more than 1tn (£800bn) officially has a new member: US chipmaker Nvidia.\\r\\nThe share price of the California-based firm shot past \$412 on Tuesday, having risen by… [+2772 chars]"))
}

val articleJsonFourth = buildJsonObject {
    put("source", sourceJson)
    put("author", encode("BBC News"))
    put("title", encode("Iowa building collapse: Woman rescued after 24 hours"))
    put("description", encode("Lisa Brooks is the ninth person rescued from a partially collapsed block in Davenport."))
    put("url", encode("http://www.bbc.co.uk/news/world-us-canada-65753750"))
    put("urlToImage", encode("https://ichef.bbci.co.uk/news/1024/branded_news/8D4A/production/_129907163_gettyimages-1494376980.jpg"))
    put("publishedAt", encode("2004-04-04T14:52:29.8237261Z"))
    put("content", encode("A woman has been pulled from the rubble of a building in Davenport, Iowa, more than 24 hours after it partially collapsed, local media report.\\r\\nLisa Brooks, 52, was rescued from the six-storey buildi… [+1921 chars]"))
}

val successJson = buildJsonObject {
    put("status", encode("ok"))
    put("totalResults", encode("4"))
    putJsonArray("articles") {
        add(articleJsonSecond)
        add(articleJsonFourth)
        add(articleJsonThird)
        add(articleJsonFirst)
    }
}

// endregion

// region: null data definitions

val sourceNullsJson = buildJsonObject {
    put("id", encode(null))
    put("name", encode("BBC News"))
}
val articleNullsJson = buildJsonObject {
    put("source", sourceNullsJson)
    put("author", encode(null))
    put("title", encode("China's C919 passenger plane makes maiden flight"))
    put("description", encode(null))
    put("url", encode("http://www.bbc.co.uk/news/world-asia-china-65737081"))
    put("urlToImage", encode(null))
    put("publishedAt", encode("2023-05-28T04:52:18.0364993Z"))
    put("content", encode(null))
}
val successNullsJson = buildJsonObject {
    put("status", encode("ok"))
    put("totalResults", encode("1"))
    putJsonArray("articles") {
        add(articleNullsJson)
    }
}
val successNoArticlesJson = buildJsonObject {
    put("status", encode("ok"))
    put("totalResults", encode("1"))
    putJsonArray("articles") {

    }
}

// endregion

// region: articles and headlines

val sourceBbc = TopStoriesResponse.ApiResponse.Article.Source(
    id = "bbc-news",
    name = "BBC News",
)
val firstArticle = TopStoriesResponse.ApiResponse.Article(
    source = sourceBbc,
    author = "BBC News",
    title = "China's C919 passenger plane makes maiden flight",
    description = "China hopes the C919 will end the dominance of Airbus and Boeing - but it relies on Western components.",
    url = "http://www.bbc.co.uk/news/world-asia-china-65737081",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/126F5/production/_129890557_0c36b96325745e2c5ab3d9927a594cbd5d7c18e7-1.jpg",
    publishedAt = Instant.parse("2001-01-01T15:52:23.652744300Z"),
    content = "China's first domestically produced passenger jet has taken off on its maiden commercial flight. ",
)
val headlineOne = Headline(
    firstArticle,
    "https://ichef.bbci.co.uk/news/1024/branded_news/126F5/production/_129890557_0c36b96325745e2c5ab3d9927a594cbd5d7c18e7-1.jpg",
)

val secondArticle = TopStoriesResponse.ApiResponse.Article(
    source = sourceBbc,
    author = "BBC News",
    title = "Will US debt ceiling deal pass? Biden and McCarthy must still walk tightrope",
    description = "Both leaders have work to do to convince their parties this is a deal to back when Congress votes.",
    url = "http://www.bbc.co.uk/news/world-us-canada-65759537",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/2F6F/production/_129934121_capitol2_976getty.jpg",
    publishedAt = Instant.parse("2002-02-02T15:52:23.652744300Z"),
    content = "When it comes to raising the US debt ceiling and avoiding a national default, reaching a deal between the two parties was an uphill battle. But there are more steep hills to climb.\nNow the two leade…",
)
val headlineTwo = Headline(
    secondArticle,
    "https://ichef.bbci.co.uk/news/1024/branded_news/2F6F/production/_129934121_capitol2_976getty.jpg",
)

val thirdArticle = TopStoriesResponse.ApiResponse.Article(
    source = sourceBbc,
    author = "BBC News",
    title = "Nvidia hits 1tn valuation as investors buy AI buzz",
    description = "Investors excited about the possibilities for artificial intelligence have sent the US chipmaker's value soaring.",
    url = "http://www.bbc.co.uk/news/business-65757812",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/9941/production/_129933293_gettyimages-1258278958.jpg",
    publishedAt = Instant.parse("2003-03-03T15:37:22.5896408Z"),
    content = "The elite club of US companies worth more than 1tn (£800bn) officially has a new member: US chipmaker Nvidia.\\r\\nThe share price of the California-based firm shot past \$412 on Tuesday, having risen by…",
)
val headlineThree = Headline(
    thirdArticle,
    "https://ichef.bbci.co.uk/news/1024/branded_news/9941/production/_129933293_gettyimages-1258278958.jpg",
)
val fourthArticle = TopStoriesResponse.ApiResponse.Article(
    source = sourceBbc,
    author = "BBC News",
    title = "Iowa building collapse: Woman rescued after 24 hours",
    description = "Lisa Brooks is the ninth person rescued from a partially collapsed block in Davenport.",
    url = "http://www.bbc.co.uk/news/world-us-canada-65753750",
    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/8D4A/production/_129907163_gettyimages-1494376980.jpg",
    publishedAt = Instant.parse("2004-04-04T14:52:29.8237261Z"),
    content = "A woman has been pulled from the rubble of a building in Davenport, Iowa, more than 24 hours after it partially collapsed, local media report.\\r\\nLisa Brooks, 52, was rescued from the six-storey buildi…",
)
val articlesList = listOf(secondArticle, fourthArticle, thirdArticle, firstArticle)

val nullsArticle = TopStoriesResponse.ApiResponse.Article(
    source = sourceBbc,
    author = "",
    title = "Lake Maggiore tourist boat carrying 20 overturns, with one dead",
    description = "",
    url = "http://www.bbc.co.uk/news/world-europe-65741465",
    urlToImage = "",
    publishedAt = Instant.parse("2023-05-28T21:07:12.5113529Z"),
    content = "",
)

// endregion