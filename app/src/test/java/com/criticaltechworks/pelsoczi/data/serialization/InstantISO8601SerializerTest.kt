package com.criticaltechworks.pelsoczi.data.serialization

import kotlinx.coroutines.runBlocking
import org.junit.Test

class InstantISO8601SerializerTest {

    @Test
    fun `ISO8601 timestring creates an instant`() = runBlocking {
        // given
        val decoded = "2023-05-28T04:52:18.0364993Z"
        // when
        val deserialized = InstantISO8601Serializer.formatDecodedTimeString(decoded)
        // then
        assert(deserialized?.toEpochMilli() == 1685249538036L)
    }

}