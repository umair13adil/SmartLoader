package com.umairadil.smartdownloaderapp.utils.recyclerViewUtils

import androidx.recyclerview.widget.RecyclerView

fun smoothScroll(recyclerView: RecyclerView, targetPos: Int) {
    val smoothScroller = FlexiSmoothScroller(recyclerView.context).setMillisecondsPerInchSearchingTarget(100f)
    smoothScroller.targetPosition = targetPos
    recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
}