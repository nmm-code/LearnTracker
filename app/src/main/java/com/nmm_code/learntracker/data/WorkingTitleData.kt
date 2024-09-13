package com.nmm_code.learntracker.data

import androidx.datastore.core.Serializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


enum class WorkingTitleType {
    UNI, SCHOOL, AP, WORK
}

@Serializable
data class WorkingTitle(
    var name: String,
    val alias: String,
    var color: Int,
    val path: String = createPath(),
    val type: WorkingTitleType
) {

    companion object {
        fun createPath(): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return '/' + (1..10)
                .map { allowedChars.random() }
                .joinToString("")

        }
    }
}

@Serializable
data class Entries(
    @Serializable(with = PersistListSerializer::class)
    var list: PersistentList<WorkingTitle> = persistentListOf()
)

@OptIn(ExperimentalSerializationApi::class)
class PersistListSerializer(
    private val serializer: KSerializer<WorkingTitle>,
) : KSerializer<PersistentList<WorkingTitle>> {

    private class PersistentListDescriptor :
        SerialDescriptor by serialDescriptor<List<WorkingTitle>>() {
        @ExperimentalSerializationApi
        override val serialName: String = "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: PersistentList<WorkingTitle>) {
        return ListSerializer(serializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PersistentList<WorkingTitle> {
        return ListSerializer(serializer).deserialize(decoder).toPersistentList()
    }
}


object EntriesSerializer : Serializer<Entries> {
    override val defaultValue: Entries
        get() = Entries()

    override suspend fun readFrom(input: InputStream): Entries {
        return try {
            Json.decodeFromString(
                deserializer = Entries.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Entries, output: OutputStream) =
        output.write(
            Json.encodeToString(
                serializer = Entries.serializer(),
                value = t
            ).encodeToByteArray()
        )

}
