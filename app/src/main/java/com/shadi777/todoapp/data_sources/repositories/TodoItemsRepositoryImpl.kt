package com.shadi777.todoapp.data_sources.repositories

import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.util.Constants
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

/**
 * Implementation of repository interface.
 *
 * @param[databaseDataSource] is used to interact with database
 * @param[networkDataSource] is used to interact with server
 */
class TodoItemsRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
    private val networkDataSource: NetworkDataSource
) : TodoItemsRepository {

    override fun getItems(): Flow<List<TodoItem>> {
        return databaseDataSource.getFlowItems()
    }

    override suspend fun refreshData(): Int {
        try {
            val response = networkDataSource.getItems()
            if (response.isSuccess) {
                val body = response.getOrNull()?.body()
                val code = response.getOrNull()?.code()
                if (body != null) {
                    databaseDataSource.updateItems(body.list)
                    val databaseData = databaseDataSource.getItems()
                    networkDataSource.updateItems(databaseData)
                }
                if (code != null) return code
            }
            return Constants.FAILED_STATUS_CODE
        } catch (exception: Exception) {
            return Constants.FAILED_STATUS_CODE
        }
    }

    override fun getUndoneItems(): Flow<List<TodoItem>> {
        return databaseDataSource.getUndoneItems()
    }

    override fun getItemById(itemId: String): TodoItem {
        return databaseDataSource.getItemById(itemId)
    }

    override suspend fun updateItems(): Int {
        try {
            val newItems = databaseDataSource.getItems()
            return networkDataSource.updateItems(newItems)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun addItem(newItem: TodoItem): Int {
        try {
            databaseDataSource.addItem(newItem)
            return networkDataSource.addItem(newItem)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun updateItem(newItem: TodoItem): Int {
        try {
            databaseDataSource.updateItem(newItem)
            return networkDataSource.updateItem(newItem)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun deleteItem(itemId: String): Int {
        try {
            databaseDataSource.deleteItemById(itemId)
            return networkDataSource.deleteItem(itemId)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getNumberOfItems(): Flow<Int> {
        return databaseDataSource.getNumberOfItems()
    }

    override suspend fun getNumberOfDoneItems(): Flow<Int> {
        return databaseDataSource.getNumberOfDoneItems()
    }
}
