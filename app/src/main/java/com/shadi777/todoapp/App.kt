package com.shadi777.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.shadi777.todoapp.di.components.AppComponent
import com.shadi777.todoapp.di.components.DaggerAppComponent
import com.shadi777.todoapp.di.modules.AppModule
import com.shadi777.todoapp.ui.ThemeMode
import com.shadi777.todoapp.util.Constants


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                        .builder()
                        .appModule(AppModule(applicationContext))
                        .build()

        ThemeMode.setCurrentMode(this, ThemeMode.getCurrentMode(context = this))

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = Constants.CHANNEL_ID
            val name = Constants.CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(id, name, importance)

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
    }
}
