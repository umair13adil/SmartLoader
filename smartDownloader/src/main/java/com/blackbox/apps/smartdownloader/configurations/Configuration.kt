package com.blackbox.apps.smartdownloader.configurations

data class Configuration(
        var maxRequests: Int,
        var maxRequestsPerHost: Int,
        var cacheSize: Long,
        var debuggable: Boolean
)