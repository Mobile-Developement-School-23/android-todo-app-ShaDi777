package com.shadi777.todoapp.network

import com.shadi777.todoapp.network.data.TodoItemResponse
import com.shadi777.todoapp.network.data.TodoListResponse
import com.shadi777.todoapp.recyclerview.data.TodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItemContainer
import retrofit2.Response
import retrofit2.http.*

interface TodoAPI {

    @GET("/todobackend/list")
    suspend fun getItems(): Response<TodoListResponse>

    @PATCH("/todobackend/list")
    suspend fun updateItems(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: List<TodoItem>
    ): Response<TodoListResponse>

    @GET("/todobackend/list/{id}")
    suspend fun getItem(
        @Path("id") id: String
    ): Response<TodoItemResponse>

    @PUT("/todobackend/list/{id}")
    suspend fun updateItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body request: TodoItemContainer
    ): Response<TodoItemResponse>

    @POST("/todobackend/list")
    suspend fun addItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: TodoItemContainer
    ): Response<TodoItemResponse>

    @DELETE("/todobackend/list/{id}")
    suspend fun removeItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoItemResponse>

}
