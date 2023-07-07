package com.shadi777.todoapp.di.components

import com.shadi777.todoapp.data_sources.models.TodoItemViewModel
import com.shadi777.todoapp.data_sources.models.TodoListViewModel
import com.shadi777.todoapp.di.FragmentCreateScope
import com.shadi777.todoapp.di.FragmentListScope
import dagger.Subcomponent

@Subcomponent
@FragmentCreateScope
interface FragmentCreateComponent {
    @FragmentCreateScope
    fun provideTodoListViewModel(): TodoListViewModel.TodoListViewModelFactory

    @FragmentListScope
    fun provideTodoItemViewModel(): TodoItemViewModel.TodoItemViewModelFactory
}
