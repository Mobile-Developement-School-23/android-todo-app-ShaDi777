package com.shadi777.todoapp.data_sources.repositories

import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.database.TodoItemsDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class that provides access to database data
 *
 * @param[todoItemsDAO] object that is used to interact with database
 */
class DatabaseDataSource(
    private val todoItemsDAO: TodoItemsDAO
) {
    suspend fun getItems(): List<TodoItem> {
        return withContext(Dispatchers.IO) {
            todoItemsDAO.getItems().map { it.toDomainModel() }
        }
    }

    fun getFlowItems(): Flow<List<TodoItem>> {
        return todoItemsDAO.getFlowItems().map { it.map { it.toDomainModel() } }
    }

    fun getUndoneItems(): Flow<List<TodoItem>> {
        return todoItemsDAO.getUndoneItems().map { it.map { it.toDomainModel() } }
    }

    fun getItemById(itemId: String): TodoItem = todoItemsDAO.getItemById(itemId).toDomainModel()

    fun getNumberOfItems(): Flow<Int> = todoItemsDAO.getNumberOfItems()

    fun getNumberOfDoneItems(): Flow<Int> = todoItemsDAO.getNumberOfDoneItems()

    suspend fun updateItems(newItems: List<TodoItem>) {
        todoItemsDAO.updateItems(newItems.map { it.toDatabaseEntity() })
    }

    suspend fun updateItem(newItem: TodoItem) {
        todoItemsDAO.updateItem(newItem.toDatabaseEntity())
    }

    suspend fun addItem(newItem: TodoItem) {
        todoItemsDAO.addItem(newItem.toDatabaseEntity())
    }

    suspend fun deleteItemById(itemId: String) {
        todoItemsDAO.deleteItemById(itemId)
    }
}
