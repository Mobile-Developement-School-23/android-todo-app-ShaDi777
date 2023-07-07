package com.shadi777.todoapp.data_sources.repositories


import com.shadi777.todoapp.data_sources.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getItems(): Flow<List<TodoItem>>

    suspend fun refreshData(): Int

    fun getUndoneItems(): Flow<List<TodoItem>>

    fun getItemById(itemId: String): TodoItem

    suspend fun updateItems(): Int

    suspend fun addItem(newItem: TodoItem): Int

    suspend fun updateItem(newItem: TodoItem): Int

    suspend fun deleteItem(itemId: String): Int

    suspend fun getNumberOfItems(): Flow<Int>
    suspend fun getNumberOfDoneItems(): Flow<Int>
}
