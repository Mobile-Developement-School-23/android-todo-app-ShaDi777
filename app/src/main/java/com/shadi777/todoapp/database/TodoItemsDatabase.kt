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

    companion object {
        @Volatile
        private var INSTANCE: TodoItemsDatabase? = null

        fun getDatabase(
            context: Context,
        ): TodoItemsDatabase {
            return INSTANCE ?: synchronized(this) {
                val NEW_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TodoItemsDatabase::class.java,
                    "todo_items_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = NEW_INSTANCE

                NEW_INSTANCE
            }

        }
    }
}
