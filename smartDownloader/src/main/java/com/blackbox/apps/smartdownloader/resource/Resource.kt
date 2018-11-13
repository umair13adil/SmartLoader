package com.blackbox.apps.smartdownloader.resource

import android.content.Context
import android.widget.ImageView
import com.blackbox.apps.smartdownloader.configurations.Configuration

data class Resource(
        var context: Context? = null,
        var url: String? = null,
        var view: ImageView? = null,
        var configuration: Configuration? = null
)