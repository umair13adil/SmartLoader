package com.umairadil.smartdownloaderapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.umairadil.smartdownloaderapp.data.Pins

class PinsDiffCallback : DiffUtil.ItemCallback<Pins>() {

    override fun areItemsTheSame(oldItem: Pins, newItem: Pins): Boolean {
        return false //Return 'false' so same items can be added again to list on "loadMore"
    }

    override fun areContentsTheSame(oldItem: Pins, newItem: Pins): Boolean {
        return false
    }
}