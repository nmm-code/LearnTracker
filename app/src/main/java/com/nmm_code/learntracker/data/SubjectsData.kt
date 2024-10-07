package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.serialization.Serializable


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