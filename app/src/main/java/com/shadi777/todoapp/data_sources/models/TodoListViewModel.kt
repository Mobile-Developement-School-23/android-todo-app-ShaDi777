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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


/**
 * View model of a list of tasks
 *
 * @param[repository] Repository that operates all tasks. It is used for updating its state
 *
 * @property[visibility] Holds last selected visibility (all tasks / only uncompleted tasks),
 * @property[itemsSize] Holds size of list of all tasks
 * @property[doneItemsSize] Holds size of list of completed tasks
 * @property[items] Holds list of tasks
 * @property[statusCode] Holds last received response status code,
 * can be accessed by [getStatusCode] function
 */
class TodoListViewModel
@AssistedInject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    @AssistedFactory
    interface TodoListViewModelFactory {
        fun create(): TodoListViewModel
    }

    class Factory(
        private val factory: TodoListViewModelFactory
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return factory.create() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean> = _visibility

    private val _itemsSize: MutableStateFlow<Int> = MutableStateFlow(0)
    val itemsSize: StateFlow<Int> = _itemsSize

    private val _doneItemsSize: MutableStateFlow<Int> = MutableStateFlow(0)
    val doneItemsSize: StateFlow<Int> = _doneItemsSize

    private val _items: MutableStateFlow<List<TodoItem>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<TodoItem>> = _items

    private val statusCode: MutableStateFlow<Int> = MutableStateFlow(Constants.OK_STATUS_CODE)

    init {
        observeItems()
        observeItemsSize()
        observeDoneItemsSize()
    }

    private fun observeItems() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                repository.getItems(),
                repository.getUndoneItems(),
                visibility
            ) { items, undoneItems, isVisible ->
                if (isVisible) {
                    items
                } else {
                    undoneItems
                }
            }.collect { combinedItems ->
                _items.value = combinedItems
            }
        }
    }

    private fun observeItemsSize() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNumberOfItems().collect {
                _itemsSize.value = it
            }
        }
    }

    private fun observeDoneItemsSize() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNumberOfDoneItems().collect {
                _doneItemsSize.value = it
            }
        }
    }

    suspend fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            statusCode.value = repository.refreshData()
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            statusCode.value = repository.deleteItem(item.id)
        }
    }

    fun updateItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            statusCode.value = repository.updateItem(item)
        }
    }

    fun getStatusCode(): StateFlow<Int> = statusCode

    fun changeStateVisibility() {
        _visibility.value = !_visibility.value
    }
}
