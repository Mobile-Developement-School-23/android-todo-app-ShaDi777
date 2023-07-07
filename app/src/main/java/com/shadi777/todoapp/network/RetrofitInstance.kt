package com.shadi777.todoapp.network

import com.shadi777.todoapp.di.AppScope
import com.shadi777.todoapp.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Object that is used to initiate Retrofit for interacting with server
 */
@Module
object RetrofitInstance {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer ${Constants.TOKEN}")
                    .build()
                chain.proceed(request)
            }.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    }
    val api: TodoAPI by lazy {
        retrofit.create(TodoAPI::class.java)
    }


    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @AppScope
    @Provides
    fun provideRetrofitClient(): Retrofit = retrofit

    @Provides
    fun provideTodoApiService(): TodoAPI = api

}
