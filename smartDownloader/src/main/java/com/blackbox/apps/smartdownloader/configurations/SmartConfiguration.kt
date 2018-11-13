package com.blackbox.apps.smartdownloader.configurations

class SmartConfiguration {

    private var maxRequests: Int = 20
    private var maxRequestsPerHost: Int = 1
    private var cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
    private var debuggable: Boolean = false

    fun setMaxNoOfRequest(noOfRequests: Int) = apply { this.maxRequests = noOfRequests }
    fun setMaxNoOfRequestPerHost(noOfRequestsPerHost: Int) = apply { this.maxRequestsPerHost = noOfRequestsPerHost }
    fun setCacheSize(size: Long) = apply { this.cacheSize = size }
    fun setDebuggable(isDebuggable: Boolean) = apply { this.debuggable = isDebuggable }

    fun build() = Configuration(
            maxRequests,
            maxRequestsPerHost,
            cacheSize,
            debuggable
    )
}