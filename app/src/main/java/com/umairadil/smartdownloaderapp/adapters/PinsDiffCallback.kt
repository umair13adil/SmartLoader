package com.umairadil.smartdownloaderapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.umairadil.smartdownloaderapp.data.Pins

class PinsDiffCallback : DiffUtil.ItemCallback<Pins>() {

    override fun areItemsTheSame(oldItem: Pins, newItem: Pins): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: Pins, newItem: Pins): Boolean {
        return oldItem.id == newItem.id
    }
}