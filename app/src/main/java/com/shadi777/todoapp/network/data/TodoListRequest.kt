package com.shadi777.todoapp.network.data

import com.shadi777.todoapp.data_sources.models.TodoItem

/**
 * Class that holds [list] to send requests
 */
data class TodoListRequest (
    val list : List<TodoItem>
)
