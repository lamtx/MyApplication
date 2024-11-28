package com.example.myapplication.misc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Serializer for a [Date] object without time and timezone.
 */
object DateOnlySerialization : KSerializer<Date> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        val s = decoder.decodeString()
        return dateFormat.parse(s)
            ?: throw ParseException("Unable to parse `$s` to Date", 0)
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(dateFormat.format(value))
    }
}