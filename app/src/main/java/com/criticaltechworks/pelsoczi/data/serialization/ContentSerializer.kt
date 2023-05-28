package com.criticaltechworks.pelsoczi.data.serialization

import androidx.annotation.VisibleForTesting
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * When the character count is more than 200 characters, the server appends the remaining
 * character count, delete the appended remaining character count message "[+168 chars]".
 */
object ContentSerializer : KSerializer<SerializableString> {

    override val descriptor: SerialDescriptor = SerializableStringDescriptor

    /**
     * [DeserializationStrategy<T>] for removing server formatted text.
     */
    override fun deserialize(decoder: Decoder): SerializableString {
        var decoded = decoder.decodeString()
        decoded = formatDecodedEllipsesCharacterCount(decoded)
        return decoded
    }

    /**
     * Note: SerializationStrategy<T> not utilized.
     */
    override fun serialize(encoder: Encoder, value: SerializableString) {
        encoder.encodeString(value)
    }

    /**
     * "The quick brown fox jumped over the ... [+8 chars]"
     * becomes
     * "The quick brown fox jumped over the ..."
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun formatDecodedEllipsesCharacterCount(decoded: String): String {
        return when {
            decoded.length > 200 -> decoded.substring(0..200).trim()
            else -> decoded.trim()
        }
    }
}