package com.shadi777.todoapp.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shadi777.todoapp.data_sources.repositories.TodoItemsRepository

/**
 * Class that is used to do repeated tasks
 */
class RefreshWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val repository: TodoItemsRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        repository.refreshData()
        return Result.success()
    }
}
