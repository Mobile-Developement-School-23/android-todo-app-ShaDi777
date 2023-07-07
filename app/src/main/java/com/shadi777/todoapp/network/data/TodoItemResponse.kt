package com.shadi777.todoapp.network.data

import com.shadi777.todoapp.data_sources.models.TodoItem

/**
 * Class that holds data of a single element received from server as a response
 */
data class TodoItemResponse(
    val status: String,
    val item: TodoItem,
    val revision: Int
)
