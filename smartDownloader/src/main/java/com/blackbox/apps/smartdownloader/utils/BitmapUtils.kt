package com.blackbox.apps.smartdownloader.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.Response
import java.io.BufferedInputStream
import java.io.File


fun createBitmapFromResponse(response: Response): Bitmap? {

    val inputStream = response.body()?.byteStream()

    if (inputStream != null) {
        val bufferedInputStream = BufferedInputStream(inputStream)
        return BitmapFactory.decodeStream(bufferedInputStream)
    }

    return null
}

fun createBitmapFromResource(resourceId: Int, context: Context): Bitmap? {
    return BitmapFactory.decodeResource(context.resources, resourceId)
}

fun createBitmapFromFile(path: String): Bitmap? {

    if (path.isNotEmpty()) {
        val image = File(path)

        if (image.exists()) {
            if (!image.isDirectory) {
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                return BitmapFactory.decodeFile(image.path, options)
            } else {
                throw Throwable("Not a valid image file.")
            }
        } else {
            throw Throwable("Image file does not exists!")
        }
    } else {
        throw Throwable("Image path provided is empty!")
    }
}

private fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        BitmapFactory.decodeResource(res, resId, this)
    }
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}