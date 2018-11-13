package com.umairadil.smartdownloaderapp

import android.app.Application
import com.blackbox.apps.smartdownloader.SmartLoader
import com.blackbox.apps.smartdownloader.configurations.SmartConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val configuration = SmartConfiguration()
                .setCacheSize(20 * 1024 * 1024) //20 MiB
                .setMaxNoOfRequest(1)
                .setDebuggable(true)
                .build()

        //Initialize Smart Loader with custom configurations
        SmartLoader().initWithConfiguration(configuration)
    }
}