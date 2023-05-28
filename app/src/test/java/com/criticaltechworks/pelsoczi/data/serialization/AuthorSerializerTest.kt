package com.criticaltechworks.pelsoczi.data.serialization

import kotlinx.coroutines.runBlocking
import org.junit.Test

class AuthorSerializerTest {

    @Test
    fun `multiple comma separated values are formatted to space author names`() = runBlocking {
        // given
        val decoded = "Eric,Zoe,Andy"
        // when
        val deserialized = AuthorSerializer.formatDecodedCsvNames(decoded)
        // then
        assert(deserialized.equals("Eric, Zoe, Andy"))
    }

    @Test
    fun `trim web address to human readable address`() = runBlocking {
        // given
        val httpsDecoded = "https://www.facebook.com/criticaltechworks/"
        val httpDecoded = "http://www.facebook.com/criticaltechworks/"
        val wwwDecoded = "http://www.facebook.com/criticaltechworks/"
        // when
        val httpsDeserialized = AuthorSerializer.formatDecodedHttpUrl(httpsDecoded)
        val httpDeserialized = AuthorSerializer.formatDecodedHttpUrl(httpDecoded)
        val wwwDeserialized = AuthorSerializer.formatDecodedHttpUrl(wwwDecoded)
        // then
        assert(true)
        assert(httpsDeserialized.equals("facebook.com/criticaltechworks"))
        assert(httpDeserialized.equals("facebook.com/criticaltechworks"))
        assert(wwwDeserialized.equals("facebook.com/criticaltechworks"))
    }

}