package com.shadi777.todoapp.recyclerview.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Action {
    NO_ACTION, SAVE_NEW, SAVE_CHANGE, DELETE
}

class SharedTodoItem : ViewModel() {
    val todoItem = MutableLiveData<TodoItem>()
    val action = MutableLiveData<Action>()

    fun updateState(newItem: TodoItem, newAction: Action) {
        todoItem.value = newItem
        action.value = newAction
    }
}