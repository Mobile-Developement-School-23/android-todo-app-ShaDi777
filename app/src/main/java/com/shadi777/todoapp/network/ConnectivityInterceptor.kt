package com.shadi777.todoapp.network

import android.content.Context
import android.database.Observable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ConnectivityInterceptor(val context: Context) : Interceptor {

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (isOnline(context)) {
            val response = chain.proceed(chain.request())
            return response
        } else {
            throw NoConnectivityException()
        }
    }
}

class NoConnectivityException : IOException() {
    override fun getLocalizedMessage(): String {
        return "No network available, please check your WiFi or Data connection"
    }

}