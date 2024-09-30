package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.serialization.Serializable


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
    override fun getList(context: Context): List<WorkingTitle> = super.read<WorkingTitle>(context)
    override fun saveList(context: Context, list: List<WorkingTitle>) = super.save<WorkingTitle>(context, list)
}