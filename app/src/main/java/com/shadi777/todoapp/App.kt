package com.shadi777.todoapp

import android.app.Application
import android.content.Context
import com.shadi777.todoapp.data_sources.repositories.DatabaseDataSource
import com.shadi777.todoapp.data_sources.repositories.NetworkDataSource
import com.shadi777.todoapp.data_sources.repositories.TodoItemsRepositoryImpl
import com.shadi777.todoapp.database.TodoItemsDatabase


class App : Application() {

    private val database by lazy {
        TodoItemsDatabase.getDatabase(this)
    }
    private val networkDataSource by lazy {
        NetworkDataSource()
    }
    private val databaseDataSource by lazy {
        DatabaseDataSource(
            database.todoItemsDAO()
        )
    }

    val todoItemsRepository by lazy {
        TodoItemsRepositoryImpl(
            databaseDataSource,
            networkDataSource
        )
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
    }
}
