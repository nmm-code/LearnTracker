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
import java.time.LocalDate


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

object WorkingTitleData : Data<WorkingTitle>(false) {
    override val fileName: String = "/working-tiles.bin"
}