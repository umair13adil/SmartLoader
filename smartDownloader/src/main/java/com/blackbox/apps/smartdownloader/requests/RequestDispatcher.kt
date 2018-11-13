package com.blackbox.apps.smartdownloader.requests

import com.blackbox.apps.smartdownloader.configurations.Configuration
import okhttp3.Dispatcher
import java.util.concurrent.Executors

class RequestDispatcher {

    companion object {

        @Volatile
        private var INSTANCE: RequestDispatcher? = null

        fun getInstance() =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: RequestDispatcher().also {
                                INSTANCE = it
                            }
                }
    }

    private var dispatcher: Dispatcher? = null

    fun createDispatcher(configuration: Configuration): Dispatcher {

        val dispatcher = Dispatcher(Executors.newFixedThreadPool(20))
        dispatcher.maxRequests = configuration.maxRequests
        dispatcher.maxRequestsPerHost = configuration.maxRequestsPerHost

        if (this.dispatcher == null)
            this.dispatcher = dispatcher

        return dispatcher
    }

    fun cancel(tag: Any) {

        dispatcher?.let {

            for (call in it.queuedCalls()) {
                if (tag == call.request().tag()) {
                    call.cancel()
                }
            }

            for (call in it.runningCalls()) {
                if (tag == call.request().tag()) {
                    call.cancel()
                }
            }
        }
    }

    fun cancelAll() {
        dispatcher?.cancelAll()
    }
}