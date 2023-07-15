package com.shadi777.todoapp.ui.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shadi777.todoapp.App
import com.shadi777.todoapp.data_sources.models.TodoItemViewModel
import com.shadi777.todoapp.data_sources.models.TodoListViewModel
import com.shadi777.todoapp.databinding.ActivityMainBinding
import com.shadi777.todoapp.network.RefreshWorker
import com.shadi777.todoapp.ui.ThemeMode
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val component by lazy {
        (applicationContext as App).appComponent.fragmentListComponent()
    }

    val itemViewModel by viewModels<TodoItemViewModel> {
        TodoItemViewModel.Factory(
            component.provideTodoItemViewModel()
        )
    }
    val listViewModel by viewModels<TodoListViewModel> {
        TodoListViewModel.Factory(
            component.provideTodoListViewModel()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeMode.setCurrentMode(this, ThemeMode.getCurrentMode(context = this))

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as App).appComponent.injectMainActivity(this)
        setUpdates()
    }

    private fun setUpdates() {
        val request =
            PeriodicWorkRequestBuilder<RefreshWorker>(
                repeatInterval = 8,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            ).setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()
        WorkManager.getInstance(this).enqueue(request)
    }
}
