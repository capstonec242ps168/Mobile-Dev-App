package com.valdo.refind.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.valdo.refind.R
import com.valdo.refind.data.remote.CraftResponse

class ListCraftAdapter(private val onItemClick: (CraftResponse) -> Unit)
    : RecyclerView.Adapter<ListCraftAdapter.CraftViewHolder>() {

    private var craftList: List<CraftResponse> = emptyList()

    fun setCrafts(crafts: List<CraftResponse>) {
        craftList = crafts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CraftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_craft, parent, false)
        return CraftViewHolder(view)
    }

    override fun onBindViewHolder(holder: CraftViewHolder, position: Int) {
        val craft = craftList[position]
        holder.bind(craft)
    }

    override fun getItemCount(): Int {
        return craftList.size
    }

    inner class CraftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val craftTitle: TextView = itemView.findViewById(R.id.craftTitle)
        private val craftImage: ImageView = itemView.findViewById(R.id.craftPhoto)

        fun bind(craft: CraftResponse) {
            craftTitle.text = craft.Crafts?.name
            if (!craft.Crafts?.image.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(craft.Crafts?.image)
                    .into(craftImage)
            }

            itemView.setOnClickListener {
                onItemClick(craft)  // Pass the full CraftResponse object
            }
        }
    }
}
