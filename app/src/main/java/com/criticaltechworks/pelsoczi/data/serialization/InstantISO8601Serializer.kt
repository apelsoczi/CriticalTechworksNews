package com.criticaltechworks.pelsoczi.data.serialization

import androidx.annotation.VisibleForTesting
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Serialization for [Instant] class.
 */
object InstantISO8601Serializer : KSerializer<Instant> {

    override val descriptor: SerialDescriptor = InstantDescriptor

    /**
     * [DeserializationStrategy<T>] for ISO 8601 formatted [Instant]
     */
    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }

    /**
     * Note: SerializationStrategy<T> not utilized.
     */
    override fun serialize(encoder: Encoder, value: Instant) {
        return encoder.encodeString(
            DateTimeFormatter.ISO_INSTANT.format(value)
        )
    }

    /**
     * Format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" to an Instant
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun formatDecodedTimeString(decoded: String): Instant? {
        return Instant.parse(decoded)
    }
}