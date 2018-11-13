package com.blackbox.apps.smartdownloader.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.Response
import java.io.BufferedInputStream


fun createBitmapFromResponse(response: Response): Bitmap? {

    val inputStream = response.body()?.byteStream()

    if (inputStream != null) {
        val bufferedInputStream = BufferedInputStream(inputStream)
        return BitmapFactory.decodeStream(bufferedInputStream)
    }

    return null
}