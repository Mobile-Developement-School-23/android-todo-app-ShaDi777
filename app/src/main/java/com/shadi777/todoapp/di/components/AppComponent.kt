package com.shadi777.todoapp.di.components

import com.shadi777.todoapp.di.AppScope
import com.shadi777.todoapp.di.modules.AppModule
import com.shadi777.todoapp.di.modules.DatabaseModule
import com.shadi777.todoapp.di.modules.RepositoryModule
import com.shadi777.todoapp.network.RetrofitInstance
import com.shadi777.todoapp.screen.MainActivity
import dagger.Component

@AppScope
@Component(
    modules = [
        RetrofitInstance::class,
        DatabaseModule::class,
        RepositoryModule::class,
        AppModule::class
    ]
)
interface AppComponent {
    fun injectMainActivity(activity: MainActivity)
    fun fragmentCreateComponent(): FragmentCreateComponent
    fun fragmentListComponent(): FragmentListComponent
}
