package com.umairadil.smartdownloaderapp.utils

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager

/*
 * Returns 'true' if connected to internet.
 */
fun isConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun setTypeface(type: Int, context: Context): Typeface {

    val assets = context.assets

    var font = Typeface.createFromAsset(assets, "fonts/Roboto-Regular.ttf")

    when (type) {
        1 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
        2 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Medium.ttf")
        3 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Regular.ttf")
        4 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf")
    }

    return font
}