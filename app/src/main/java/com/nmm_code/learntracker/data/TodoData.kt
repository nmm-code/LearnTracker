package com.nmm_code.learntracker.data

import kotlinx.serialization.Serializable


@Serializable
data class Todo(
    val title: String,
    val message: List<String>,
)


object TodoData : Data<Todo>() {
    override val fileName: String = "/todos.bin"
}