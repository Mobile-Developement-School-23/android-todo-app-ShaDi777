package com.shadi777.todoapp.network.data

import com.shadi777.todoapp.data_sources.models.TodoItem

/**
 * Class that holds [element] to send requests
 */
data class TodoItemRequest (
    val element: TodoItem
)
