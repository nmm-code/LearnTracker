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
    override fun getList(context: Context): List<Todo> = super.read<Todo>(context)
    override fun saveList(context: Context, list: List<Todo>) = super.save<Todo>(context, list)
}