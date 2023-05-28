package com.criticaltechworks.pelsoczi.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

val kotlinxJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

fun encode(anything: String?): JsonElement = kotlinxJson.encodeToJsonElement(anything)

/**
 * Utility function to compare contents of a [String] primitive and a JsonLiteral (which acts as
 * a wrapper class around a primitive, but has no accessor function).
 *
 * @param other the primitive to compare as an encoded JsonElement.
 *
 * @return the two JsonElements encoded have the same primitive value.
 */
fun JsonElement?.equalTo(other: String): Boolean {
    return this?.equals(encode(other)) ?: false
}