package com.example.mc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mc.R
import com.example.mc.model.Event

class EventListAdapter: RecyclerView.Adapter<EventListAdapter.ViewHolder>() {
    var events =  listOf<Event>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.location?.text = events[position].location
        holder.date?.text = events[position].data
    }

    override fun getItemCount() = events.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val location = itemView.findViewById<TextView>(R.id.locationAddress)
        val date = itemView.findViewById<TextView>(R.id.date)
    }
}
