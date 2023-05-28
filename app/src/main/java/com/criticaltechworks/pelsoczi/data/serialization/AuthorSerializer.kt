package com.criticaltechworks.pelsoczi.data.serialization

import androidx.annotation.VisibleForTesting
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Various article author names are poorly formatted by the server. Multiple comma separated
 * author names are not spaced between the ",". https urls as author names are also formatted
 * in non-human readable format, "https://www." instead of "site.come".
 */
object AuthorSerializer : KSerializer<SerializableString> {

    override val descriptor: SerialDescriptor = SerializableStringDescriptor

    /**
     * [DeserializationStrategy<T>] for cleaning up multiple author names, and urls.
     */
    override fun deserialize(decoder: Decoder): SerializableString {
        var decoded = decoder.decodeString()
        decoded = formatDecodedCsvNames(decoded)
        decoded = formatDecodedHttpUrl(decoded)
        return decoded
    }

    /**
     * Note: SerializationStrategy<T> not utilized.
     */
    override fun serialize(encoder: Encoder, value: SerializableString) {
        encoder.encodeString(value)
    }

    /**
     * add spacing in between author names "Eric,Zoe,Andy" to "Eric, Zoe, Andy"
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun formatDecodedCsvNames(decoded: String): String {
        var decoded = decoded
        if (decoded.contains(",")) {
            decoded = decoded.replace(",", ", ")
        }
        return decoded
    }

    /**
     * cleanup http url author name "https://www.facebook.com/kslcom/" to "facebook.com/kslcom"
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun formatDecodedHttpUrl(decoded: String): String {
        var decoded = decoded
        if (decoded.contains("https://www.")) {
            decoded = decoded.replace("https://www.", "")
            if (decoded.last() == '/') {
                decoded = decoded.substring(0..decoded.length-2)
            }
        }
        if (decoded.contains("http://www.")) {
            decoded = decoded.replace("http://www.", "")
            if (decoded.last() == '/') {
                decoded = decoded.substring(0..decoded.length-2)
            }
        }
        if (decoded.contains("www.")) {
            decoded = decoded.replace("www.", "")
            if (decoded.last() == '/') {
                decoded = decoded.substring(0..decoded.length-2)
            }
        }
        return decoded
    }

}