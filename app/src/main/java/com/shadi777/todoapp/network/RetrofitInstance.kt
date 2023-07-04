package com.shadi777.todoapp.network

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    const val TOKEN = "upseize"


    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
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

}

