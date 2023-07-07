package com.shadi777.todoapp.network

import com.shadi777.todoapp.network.data.TodoItemRequest
import com.shadi777.todoapp.network.data.TodoItemResponse
import com.shadi777.todoapp.network.data.TodoListRequest
import com.shadi777.todoapp.network.data.TodoListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interface with all needed functions to interact with server
 */
interface TodoAPI {

    @GET("list")
    suspend fun getItems(): Response<TodoListResponse>

    @PATCH("list")
    suspend fun updateItems(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: TodoListRequest
    ): Response<TodoListResponse>

    @GET("list/{id}")
    suspend fun getItem(
        @Path("id") id: String
    ): Response<TodoItemResponse>

    @PUT("list/{id}")
    suspend fun updateItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body request: TodoItemRequest
    ): Response<TodoItemResponse>

    @POST("list")
    suspend fun addItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: TodoItemRequest
    ): Response<TodoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoItemResponse>

}
