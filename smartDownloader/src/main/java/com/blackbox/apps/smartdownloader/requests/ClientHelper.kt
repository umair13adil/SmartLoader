package com.blackbox.apps.smartdownloader.requests

import android.content.Context
import com.blackbox.apps.smartdownloader.cache.CacheHelper
import com.blackbox.apps.smartdownloader.configurations.Configuration
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ClientHelper {

    fun createClient(mConfiguration: Configuration, context: Context): OkHttpClient {

        //Create dispatcher
        val dispatcher = RequestDispatcher.getInstance().createDispatcher(mConfiguration)

        val okHttpClient = OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .cache(CacheHelper.setUpCache(context, mConfiguration))
                .connectionPool(ConnectionPool(100, 30, TimeUnit.SECONDS))
                .build()

        return okHttpClient
    }
}