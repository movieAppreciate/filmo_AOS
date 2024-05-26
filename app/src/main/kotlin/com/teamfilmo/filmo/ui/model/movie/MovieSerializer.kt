package com.teamfilmo.filmo.ui.model.movie

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

object MovieSerializer : KSerializer<MovieProviders> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("MovieProviders") {
            element<MovieProviders.Providers1>("type1")
            element<MovieProviders.Providers2>("type2")
        }

    override fun deserialize(decoder: Decoder): MovieProviders {
        val input =
            decoder.decodeStructure(descriptor) {
                var result: MovieProviders? = null
                while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        0 -> result = decodeSerializableElement(descriptor, 0, MovieProviders.Providers1.serializer())
                        1 -> result = decodeSerializableElement(descriptor, 1, MovieProviders.Providers2.serializer())
                        CompositeDecoder.DECODE_DONE -> break
                        else -> throw SerializationException("Unexpected index $index")
                    }
                }
                result ?: throw SerializationException("MovieProviders was not deserialized")
            }
        return input
    }

    override fun serialize(
        encoder: Encoder,
        value: MovieProviders,
    ) {
        when (value) {
            is MovieProviders.Providers1 -> encoder.encodeSerializableValue(MovieProviders.Providers1.serializer(), value)
            is MovieProviders.Providers2 -> encoder.encodeSerializableValue(MovieProviders.Providers2.serializer(), value)
        }
    }
}
