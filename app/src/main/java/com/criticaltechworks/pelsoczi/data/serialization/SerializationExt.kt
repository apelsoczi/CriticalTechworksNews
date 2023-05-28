package com.criticaltechworks.pelsoczi.data.serialization

import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * Defined to bypass kotlinx "StringSerializer already exists" error message when
 * specifying a [String] serializer with the `@Serializable(with = ...)` annotation.
 *
 * Allows KSerializer<String> custom deserialization and serialization string operations.
 */
typealias SerializableString = String

/**
 * [SerialDescriptor] for typesafe [SerializableString]
 */
val SerializableStringDescriptor = PrimitiveSerialDescriptor(
    serialName = "SerializableString",
    kind = PrimitiveKind.STRING
)

/**
 * [SerialDescriptor] for [Instant]
 */
val InstantDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
