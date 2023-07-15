package com.shadi777.todoapp.data_sources.models

import android.app.Notification.Action
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shadi777.todoapp.data_sources.repositories.TodoItemsRepository
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.model.TodoAction
import com.shadi777.todoapp.util.Constants
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

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

    var isUpdating = false

    private val selectedItem: MutableStateFlow<TodoItem> =
        MutableStateFlow(
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "",
                priority = Priority.Default,
                isDone = false,
                color = null,
                createDate = Date().time,
                changeDate = Date().time
            )
        )
    fun getSelectedItem(): StateFlow<TodoItem> = selectedItem.asStateFlow()

    private val deletedItem: MutableStateFlow<TodoItem?> = MutableStateFlow(null)

    fun getDeletedItem(): StateFlow<TodoItem?> = deletedItem.asStateFlow()

    private val statusCode: MutableStateFlow<Int> = MutableStateFlow(Constants.OK_STATUS_CODE)
    fun getStatusCode(): StateFlow<Int> = statusCode

    fun selectItem(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            when (id) {
                null -> {
                    selectedItem.value =
                        TodoItem(
                            id = UUID.randomUUID().toString(),
                            text = "",
                            priority = Priority.Default,
                            isDone = false,
                            color = null,
                            createDate = Date().time,
                            changeDate = Date().time
                        )
                    isUpdating = false
                }
                else -> {
                    selectedItem.value = repository.getItemById(id)
                    isUpdating = true
                }
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

    fun onAction(action: TodoAction) {
        when(action) {
            is TodoAction.UpdateText -> selectedItem.update { it.copy(text = action.text) }
            is TodoAction.UpdatePriority -> selectedItem.update { it.copy(priority = action.priority) }
            is TodoAction.UpdateDate -> selectedItem.update { it.copy(deadlineDate = action.date) }
            is TodoAction.SaveTask -> {
                if (isUpdating) updateItem(item = selectedItem.value)
                else addItem(item = selectedItem.value)
            }
            is TodoAction.DeleteTask ->{
                deletedItem.value = selectedItem.value
                deleteItem(selectedItem.value.id)
            }
        }
    }
}
