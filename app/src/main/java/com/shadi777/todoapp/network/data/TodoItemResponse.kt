package com.shadi777.todoapp.network.data

import com.shadi777.todoapp.recyclerview.data.TodoItem

data class TodoItemResponse(
    val status: String,
    val item: TodoItem,
    val revision: Int
)