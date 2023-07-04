package com.shadi777.todoapp.network.data

import com.shadi777.todoapp.recyclerview.data.TodoItem

data class TodoListResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)