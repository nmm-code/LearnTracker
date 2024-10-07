package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.io.File

@Serializable
abstract class Data<T>(val local: Boolean = true) {
    abstract val fileName: String
    abstract fun getList(context: Context): List<T>
    abstract fun saveList(context: Context, list: List<T>)

    abstract var list: List<T>?

    @OptIn(ExperimentalSerializationApi::class)
    protected inline fun <reified T> read(context: Context): List<T> {
        var dir = ""
        if (local)
            runBlocking {
                dir = DataStoreState(context, DataStoreState.PATH).get("")
            }

        val file = File(context.filesDir.path + dir + fileName)
        if (!file.exists()) {
            return emptyList()
        }

        val fileContent = file.readBytes()
        return Cbor.decodeFromByteArray(fileContent)
    }

    @OptIn(ExperimentalSerializationApi::class)
    protected inline fun <reified T> save(context: Context, list: List<T>) {
        var dir = ""
        if (local)
            runBlocking {
                dir = DataStoreState(context, DataStoreState.PATH).get("")
            }

        val file = File(context.filesDir.path + dir + fileName)
        file.writeBytes(Cbor.encodeToByteArray(list))
    }

    fun clear() {
        list = null
    }
}

