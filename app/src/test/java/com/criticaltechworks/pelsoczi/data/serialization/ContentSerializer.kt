package com.criticaltechworks.pelsoczi.data.serialization

import kotlinx.coroutines.runBlocking
import org.junit.Test

class ContentSerializerTest {

    @Test
    fun `format text to content length without ellipses character count`() = runBlocking {
        // given
        val decoded = "China's first domestically produced passenger jet has taken off on its maiden commercial flight. \\r\\nState TV showed the C919 rising into the skies above Shanghai, heading to the capital Beijing early … [+960 chars]"
        // when
        val deserialized = ContentSerializer.formatDecodedEllipsesCharacterCount(decoded)
        // then
        assert(deserialized.contains("[+960 chars]").not())
        assert(deserialized.length == 200)
    }

}