package com.umairadil.smartdownloaderapp.data.network

import android.util.Log
import com.umairadil.smartdownloaderapp.BuildConfig
import com.umairadil.smartdownloaderapp.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    private val TAG = "RestClient"

    fun apiService(): API {
        return createRestAdapter().create(API::class.java)
    }

    private fun createRestAdapter(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (BuildConfig.DEBUG)
                Log.i(TAG, message)
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}