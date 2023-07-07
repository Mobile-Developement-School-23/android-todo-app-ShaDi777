package com.shadi777.todoapp.network.data

import com.shadi777.todoapp.data_sources.models.TodoItem

/**
 * Class that holds data of a list of elements received from server as a response
 */
data class TodoListResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)
