package com.shadi777.todoapp.data_sources.repositories

import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.network.RetrofitInstance
import com.shadi777.todoapp.network.TodoAPI
import com.shadi777.todoapp.network.data.TodoItemRequest
import com.shadi777.todoapp.network.data.TodoItemResponse
import com.shadi777.todoapp.network.data.TodoListRequest
import com.shadi777.todoapp.network.data.TodoListResponse
import com.shadi777.todoapp.util.Constants
import retrofit2.Response
import java.lang.Exception

/**
 * Class that provides access to remote data
 *
 * @property[api] Object that is used to interact with server
 * @property[lastRevision] Last revision received from a server. It is used to send new requests
 */
class NetworkDataSource {
    private val api: TodoAPI = RetrofitInstance.api
    private var lastRevision: Int = 0

    suspend fun getItemById(itemId: String): Response<TodoItemResponse> {
        return try {
            val response = api.getItem(itemId)
            if (response.isSuccessful) lastRevision = response.body()!!.revision
            response
        } catch (exception: Exception) {
            throw exception
        }
    }

    suspend fun addItem(item: TodoItem): Int =
        try {
            val response = api.addItem(lastRevision, TodoItemRequest(item))
            if (response.isSuccessful) lastRevision = response.body()!!.revision
            response.code()
        } catch (exception: Exception) {
            Constants.FAILED_STATUS_CODE
        }

    suspend fun updateItems(newItems: List<TodoItem>): Int =
        try {
            val response = api.updateItems(lastRevision, TodoListRequest(newItems))
            if (response.isSuccessful) lastRevision = response.body()!!.revision
            response.code()
        } catch (exception: Exception) {
            Constants.FAILED_STATUS_CODE
        }

    suspend fun getItems(): Result<Response<TodoListResponse>> =
        try {
            val response = api.getItems()
            if (response.isSuccessful) lastRevision = response.body()!!.revision
            Result.success(response)
        } catch (exception: Exception) {
            Result.failure(exception)
        }

    suspend fun updateItem(newItem: TodoItem): Int =
        try {
            val response = api.updateItem(lastRevision, newItem.id, TodoItemRequest(newItem))
            if (response.isSuccessful) lastRevision = response.body()!!.revision
            response.code()
        } catch (exception: Exception) {
            Constants.FAILED_STATUS_CODE
        }

    suspend fun deleteItem(id: String): Int =
        try {
            val response = api.deleteItem(lastRevision, id)
            if (response.isSuccessful) lastRevision = response.body()!!.revision
            response.code()
        } catch (exception: Exception) {
            Constants.FAILED_STATUS_CODE
        }
}
