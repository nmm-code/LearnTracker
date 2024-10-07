package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.DayOfWeek


@Serializable
data class Subject(
    val title: String,
    val color: Int,
)


object SubjectsData : Data<Subject>() {
    override val fileName: String = "/subject.bin"
    override var list: List<Subject>? = null

    override fun getList(context: Context): List<Subject> {
        if (list == null)
            list = super.read(context)

        return list as List<Subject>
    }

    override fun saveList(context: Context, list: List<Subject>) {
        this.list = list

        super.save(context, list)
    }
}