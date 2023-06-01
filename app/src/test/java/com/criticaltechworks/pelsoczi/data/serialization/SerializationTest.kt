package com.criticaltechworks.pelsoczi.data.serialization

import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse
import com.criticaltechworks.pelsoczi.util.equalTo
import com.criticaltechworks.pelsoczi.util.kotlinxJson
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import org.junit.Test

class SerializationTest {

    private val json = kotlinxJson

    @Test
    fun `kotlinx configuration supports server`() = runBlocking {
        assert(kotlinxJson.configuration.ignoreUnknownKeys == true)
        assert(kotlinxJson.configuration.coerceInputValues == true)
    }

    @Test
    fun `values are deserialized`() = runBlocking {
        // given
        val jsonString = successJson
//
        // when
        val apiResponse = json.decodeFromString<ApiResponse>(jsonString.toString())
        val articlesJsonArray = successJson.getValue("articles") as JsonArray
        val articleJson = articlesJsonArray.first() as JsonObject
        val sourceJson = articleJson["source"] as JsonObject

        // then
        assert(sourceJson["id"].equalTo(apiResponse.articles.first().source.id))
        assert(sourceJson["name"].equalTo(apiResponse.articles.first().source.name))
        // author deserializer test
        assert(articleJson["title"].equalTo(apiResponse.articles.first().title))
        assert(articleJson["description"].equalTo(apiResponse.articles.first().description))
        assert(articleJson["url"].equalTo(apiResponse.articles.first().url))
        assert(articleJson["urlToImage"].equalTo(apiResponse.articles.first().urlToImage))
        // published at iso 8601 instant deserializer test
        // content deserializer test
    }

    @Test
    fun `null server values are deserialized to empty strings`() = runBlocking {
        // given
        val jsonString = successNullsJson

        // when
        val apiResponse = json.decodeFromString<ApiResponse>(jsonString.toString())
        val articlesJsonArray = successNullsJson.getValue("articles") as JsonArray
        val articleJson = articlesJsonArray.first() as JsonObject
        val sourceJson = articleJson["source"] as JsonObject

        // then
        assert(apiResponse.articles.first().source.id.equals(""))
        assert(apiResponse.articles.first().author.equals(""))
        assert(apiResponse.articles.first().description.equals(""))
        assert(apiResponse.articles.first().urlToImage.equals(""))
        assert(apiResponse.articles.first().content.equals(""))
    }

}


