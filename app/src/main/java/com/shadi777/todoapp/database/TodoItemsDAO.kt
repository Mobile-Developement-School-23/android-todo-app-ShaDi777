package com.shadi777.todoapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


/**
 * Interface with all needed functions to interact with database
 */
@Dao
interface TodoItemsDAO {

    @Query("SELECT * FROM TodoItemEntity")
    fun getItems(): List<TodoItemEntity>

    @Query("SELECT COUNT(*) FROM TodoItemEntity")
    fun getNumberOfItems(): Flow<Int>

    @Query("SELECT COUNT(*) FROM TodoItemEntity WHERE done = 1")
    fun getNumberOfDoneItems(): Flow<Int>

    @Query("SELECT * FROM TodoItemEntity")
    fun getFlowItems(): Flow<List<TodoItemEntity>>

    @Query("SELECT * FROM TodoItemEntity WHERE done = 0")
    fun getUndoneItems(): Flow<List<TodoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItems(newItems: List<TodoItemEntity>)

    @Query("SELECT * FROM TodoItemEntity WHERE id = :itemId")
    fun getItemById(itemId: String): TodoItemEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(newItem: TodoItemEntity)

    @Update
    suspend fun updateItem(newItem: TodoItemEntity)

    @Query("DELETE FROM TodoItemEntity WHERE id = :itemId")
    suspend fun deleteItemById(itemId: String)

}
