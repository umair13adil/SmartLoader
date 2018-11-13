package com.blackbox.apps.smartdownloader.resource

import android.graphics.Bitmap

interface ResourceCallback {

    fun onCachedLoaded(bitmap: Bitmap)

    fun onLoadFailed(e: Exception)
}