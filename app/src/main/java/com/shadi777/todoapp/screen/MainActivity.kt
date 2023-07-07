package com.shadi777.todoapp.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.shadi777.todoapp.App
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.TodoItemViewModel
import com.shadi777.todoapp.data_sources.models.TodoItemViewModelFactory
import com.shadi777.todoapp.data_sources.models.TodoListViewModel
import com.shadi777.todoapp.data_sources.models.TodoListViewModelFactory
import com.shadi777.todoapp.databinding.ActivityMainBinding
import com.shadi777.todoapp.network.RefreshWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val todoItemsRepository by lazy {
        (applicationContext as App).todoItemsRepository
    }
    val itemViewModel by viewModels<TodoItemViewModel> {
        TodoItemViewModelFactory(todoItemsRepository)
    }
    val listViewModel by viewModels<TodoListViewModel> {
        TodoListViewModelFactory(todoItemsRepository)
    }

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternet()
        setUpdates()
    }

    private fun checkInternet() {
        val contextView = binding.root.rootView
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Snackbar.make(contextView, getString(R.string.internet_found), Snackbar.LENGTH_LONG).show()
                lifecycleScope.launch(Dispatchers.IO) {
                    todoItemsRepository.updateItems()
                }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Snackbar.make(contextView, getString(R.string.internet_not_found), Snackbar.LENGTH_LONG).show()
            }

        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun setUpdates() {
        val request = PeriodicWorkRequestBuilder<RefreshWorker>(
            repeatInterval = 8,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()
        WorkManager.getInstance(this).enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
