package com.shadi777.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import com.shadi777.todoapp.database.TodoItemsDAO
import com.shadi777.todoapp.database.TodoItemsDatabase
import com.shadi777.todoapp.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {
    @AppScope
    @Provides
    fun provideTodoDAO(database: TodoItemsDatabase): TodoItemsDAO {
        return database.todoItemsDAO()
    }

    @AppScope
    @Provides
    fun provideDatabase(context: Context): TodoItemsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TodoItemsDatabase::class.java,
            "todo_items_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
