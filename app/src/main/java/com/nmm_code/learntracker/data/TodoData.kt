package com.nmm_code.learntracker.data

import android.content.Context
import kotlinx.serialization.Serializable


@Serializable
data class Todo(
    val title: String,
    val message: List<String>,
)


object TodoData : Data<Todo>() {
    override val fileName: String = "/todos.bin"

    override var list: List<Todo>? = null

    override fun getList(context: Context): List<Todo> {
        if (list == null)
            list = super.read(context)

        return list as List<Todo>
    }

    override fun saveList(context: Context, list: List<Todo>) {
        this.list = list

        super.save(context, list)
    }
}