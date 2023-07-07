package com.shadi777.todoapp.di.modules

import com.shadi777.todoapp.data_sources.repositories.TodoItemsRepository
import com.shadi777.todoapp.data_sources.repositories.TodoItemsRepositoryImpl
import com.shadi777.todoapp.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppScope
    @Binds
    fun bindTodoRepository(todoItemsRepositoryImpl: TodoItemsRepositoryImpl): TodoItemsRepository
}
