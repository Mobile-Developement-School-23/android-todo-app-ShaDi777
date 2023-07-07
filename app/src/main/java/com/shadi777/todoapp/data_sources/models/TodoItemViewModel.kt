package com.shadi777.todoapp.data_sources.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shadi777.todoapp.data_sources.repositories.TodoItemsRepository
import com.shadi777.todoapp.util.Constants
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * View model of a single task
 *
 * @param[repository] Repository that operates all tasks. It is used for updating its state
 *
 * @property[selectedItem] Holds currently selected task,
 * can be accessed by [getSelectedItem] function
 *
 * @property[statusCode] Holds last received response status code,
 * can be accessed by [getStatusCode] function
 */
class TodoItemViewModel
@AssistedInject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    @AssistedFactory
    interface TodoItemViewModelFactory {
        fun create(): TodoItemViewModel
    }
    class Factory(
        private val factory: TodoItemViewModelFactory
        ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoItemViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return factory.create() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val selectedItem: MutableStateFlow<TodoItem?> = MutableStateFlow(null)
    fun getSelectedItem(): StateFlow<TodoItem?> = selectedItem

    private val statusCode: MutableStateFlow<Int> = MutableStateFlow(Constants.OK_STATUS_CODE)
    fun getStatusCode(): StateFlow<Int> = statusCode

    fun selectItem(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            when (id) {
                null -> selectedItem.value = null
                else -> selectedItem.value = repository.getItemById(id)
            }
        }
    }

    fun addItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            statusCode.value = repository.addItem(item)
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            statusCode.value = repository.deleteItem(itemId)
        }
    }

    fun updateItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            statusCode.value = repository.updateItem(item)
        }
    }
}
