package com.blackbox.apps.smartdownloader.resource

import android.content.Context
import android.widget.ImageView
import com.blackbox.apps.smartdownloader.configurations.Configuration

data class Resource(
        var context: Context? = null,
        var url: String? = null,
        var resourceId: Int? = null,
        var view: ImageView? = null,
        var configuration: Configuration? = null
)