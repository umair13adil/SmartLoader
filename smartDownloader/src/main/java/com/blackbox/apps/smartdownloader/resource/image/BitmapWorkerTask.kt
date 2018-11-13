package com.blackbox.apps.smartdownloader.resource.image

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import com.blackbox.apps.smartdownloader.cache.MemoryCache.addBitmapToMemoryCache
import com.blackbox.apps.smartdownloader.utils.createBitmapFromResponse
import okhttp3.Response
import java.lang.ref.WeakReference

internal class BitmapWorkerTask(private val path: String, imageView: ImageView) : AsyncTask<Response, Void, Bitmap>() {

    private val imageViewReference: WeakReference<ImageView>?

    init {
        //Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = WeakReference(imageView)
    }

    override fun doInBackground(vararg params: Response): Bitmap? {
        val bitmap = createBitmapFromResponse(params[0])
        addBitmapToMemoryCache(path, bitmap)
        return bitmap
    }

    //onPostExecute() sets the bitmap fetched by doInBackground()
    override fun onPostExecute(bitmap: Bitmap?) {
        if (imageViewReference != null && bitmap != null) {
            val imageView = imageViewReference.get() as ImageView
            imageView.setImageBitmap(bitmap)
        }
    }
}