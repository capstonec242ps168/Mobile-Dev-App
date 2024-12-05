package com.valdo.refind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.valdo.refind.R

data class TrashItem(val type: String, val treatment: String?)

class TrashAdapter(private val items: List<TrashItem>) :
    RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {

    class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.textView_type)
        val treatmentTextView: TextView = itemView.findViewById(R.id.textView_treatment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trash, parent, false)
        return TrashViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        val item = items[position]
        holder.typeTextView.text = item.type
        holder.treatmentTextView.text = item.treatment ?: "No Treatment"
    }

    override fun getItemCount(): Int = items.size
}
