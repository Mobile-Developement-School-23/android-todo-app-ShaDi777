package com.shadi777.todoapp.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shadi777.todoapp.App
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.TodoItemViewModel
import com.shadi777.todoapp.data_sources.models.TodoListViewModel
import com.shadi777.todoapp.databinding.ActivityMainBinding
import com.shadi777.todoapp.network.RefreshWorker
import com.shadi777.todoapp.ui.ThemeMode
import com.shadi777.todoapp.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var pLauncher: ActivityResultLauncher<String>

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
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as App).appComponent.injectMainActivity(this)
        sharedPreferences = getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE)
        setUpdates()
        registerPermissionListener()
        checkNotificationPermission()

        if (intent != null) {
            val id = intent.getStringExtra(Constants.TASK_ID_KEY) ?: ""
            if (id.isEmpty()) return
            itemViewModel.selectItem(id)
            itemViewModel.fromIntent = true
        }
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

    private fun saveNotificationsPermission(result: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(Constants.NOTIFICATION_KEY, result)
            apply()
        }
    }

    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                saveNotificationsPermission(true)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {}
            else -> {
                pLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                saveNotificationsPermission(true)
            } else {
                saveNotificationsPermission(false)
                Toast.makeText(this, R.string.notifications_disabled, Toast.LENGTH_LONG).show()
            }
        }
    }
}
