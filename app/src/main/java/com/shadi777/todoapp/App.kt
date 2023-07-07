package com.shadi777.todoapp

import android.app.Application
import android.content.Context
import com.shadi777.todoapp.di.components.AppComponent
import com.shadi777.todoapp.di.components.DaggerAppComponent
import com.shadi777.todoapp.di.modules.AppModule


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                        .builder()
                        .appModule(AppModule(applicationContext))
                        .build()
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
    }
}
