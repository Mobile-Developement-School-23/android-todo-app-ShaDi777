package com.shadi777.todoapp

import android.app.Application
import android.content.Context
import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository
import kotlinx.coroutines.runBlocking

class App : Application() {

    val todoItemsRepository = TodoItemsRepository()

    companion object {
        fun get(context: Context): App = context.applicationContext as App
    }
}