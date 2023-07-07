package com.shadi777.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Class that holds instance of database
 */
@Database(entities = [TodoItemEntity::class], version = 1, exportSchema = false)
abstract class TodoItemsDatabase : RoomDatabase() {
    abstract fun todoItemsDAO(): TodoItemsDAO
}
