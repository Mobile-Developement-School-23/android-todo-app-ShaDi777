package com.shadi777.todoapp.recyclerview.data

import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.shadi777.todoapp.network.RetrofitInstance
import com.shadi777.todoapp.screen.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Error
import java.util.Date


class TodoItemsRepository {
    /*
    companion object {
        var idIterator: Long = 0
        val itemlist = mutableListOf<TodoItem>()
    }

    init {
        for(i in 1..6) {
            val id: String = idIterator.toString()
            val text = "Пример дела #${i}"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 7..12) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nЕсть 2 строка"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 13..18) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nЕсть 2 строка\nЕсть 3 строка"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 19..24) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nВ этом деле много слов, поэтому после переполнения должно стоять многоточие"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 25..30) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nС дедлайном на завтра"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()
            val deadline_date: Date = Date()
            deadline_date.time += 86400 * 1000

            val todoItem = TodoItem(id, text, priority, is_done, create_date, deadline_date)
            addTodoItem(todoItem)
        }
    }

    fun addTodoItem(item: TodoItem) {
        idIterator++
        itemlist.add(item)
    }

    fun removeTodoItem(item: TodoItem) = itemlist.removeIf { item.id == it.id }

    fun countDone(): Int = itemlist.count { it.is_done }

    fun getTasks(isDoneVisible: Boolean): List<TodoItem> {
        if (isDoneVisible) {
            return itemlist
        } else {
            return buildList {
                for (item in itemlist)
                    if (!item.is_done)
                        add(item)
            }
        }
    }

    */

    val api = RetrofitInstance.api
    var last_rev: Int = 0
    var itemlist = mutableListOf<TodoItem>()
    var idIterator: Long = 0

    init {
        runBlocking {
            // TODO Check internet
            loadFromServer()
        }
    }

    suspend fun loadFromServer() {
        val response = api.getItems()

        // TODO show error
        if (!response.isSuccessful) {
            Log.d("Network", "Not successful load!")

            return
        }
        Log.d("Network", "Successful load!")
        // TODO end of todo

        itemlist = response.body()!!.list.toMutableList()
        last_rev = response.body()!!.revision
        idIterator = itemlist.maxOf { it.id.toLong() } + 1
    }

    suspend fun addTodoItem(item: TodoItem) {
        val itemContainer = TodoItemContainer(item)
        val response = api.addItem(last_rev, itemContainer)

        // TODO show error
        if (!response.isSuccessful) {
            Log.d("Network", "Not successful ADD!")

            // throw Error()
            return
        }
        Log.d("Network", "Successful ADD!")
        // TODO end of todo


        itemlist.add(item)
        last_rev = response.body()!!.revision
        idIterator++
    }

    suspend fun removeTodoItem(item: TodoItem) {
        val response = api.removeItem(last_rev, item.id)

        // TODO show error
        if (!response.isSuccessful) {
            Log.d("Network", "Not successful REMOVE!")

            // throw Error()
            return
        }
        Log.d("Network", "Successful REMOVE!")
        // TODO end of todo

        itemlist.removeIf { item.id == it.id }
        last_rev = response.body()!!.revision
    }

    suspend fun updateTodoItem(item: TodoItem) {
        val itemContainer = TodoItemContainer(item)
        val response = api.updateItem(last_rev, item.id, itemContainer)

        // TODO show error
        if (!response.isSuccessful) {
            Log.d("Network", "Not successful UPDATE!")

            // throw Error()
            return
        }
        Log.d("Network", "Successful UPDATE!")
        // TODO end of todo

        last_rev = response.body()!!.revision
    }

    fun countDone(): Int = itemlist.count { it.is_done }

    fun getTasks(isDoneVisible: Boolean): List<TodoItem> {
        if (isDoneVisible) {
            return itemlist.toList()
        } else {
            return buildList {
                for (item in itemlist)
                    if (!item.is_done)
                        add(item)
            }
        }
    }


}