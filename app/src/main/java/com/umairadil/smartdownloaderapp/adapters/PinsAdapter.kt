package com.umairadil.smartdownloaderapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackbox.apps.smartdownloader.SmartLoader
import com.umairadil.smartdownloaderapp.R
import com.umairadil.smartdownloaderapp.data.Pins
import com.umairadil.smartdownloaderapp.utils.setTypeface
import kotlinx.android.synthetic.main.list_item_pin.view.*


class PinsAdapter : ListAdapter<Pins, PinsAdapter.ViewHolder>(PinsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.list_item_pin, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pin = getItem(position)

        holder.apply {
            bind(pin)
            itemView.tag = pin
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pin: Pins) {
            val context = itemView.context

            itemView.txt_user_name.text = pin.user_name
            itemView.txt_user_name.typeface = setTypeface(4, context)

            SmartLoader().withContext(itemView.context).load(pin.url).into(itemView.img_pin)
            SmartLoader().withContext(itemView.context).load(pin.user_profile_image).into(itemView.profile_image)
        }
    }
}