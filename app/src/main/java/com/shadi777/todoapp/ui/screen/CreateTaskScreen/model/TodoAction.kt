package com.shadi777.todoapp.ui.screen.CreateTaskScreen.model

import com.shadi777.todoapp.data_sources.models.Priority

sealed class TodoAction {
    data class UpdateText(val text: String): TodoAction()
    data class UpdatePriority(val priority: Priority): TodoAction()
    data class UpdateDate(val date: Long): TodoAction()
    //data class UpdateTime(val hour: Int, val minute: Int, val second: Int): TodoAction()
    object SaveTask: TodoAction()
    object DeleteTask : TodoAction()
}