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
import com.valdo.refind.data.remote.NewsItem
import com.valdo.refind.data.remote.NewsResponse

class ListNewsAdapter(
    private var newsList: List<NewsItem>,
    private val onItemClick: (NewsItem) -> Unit) :
    RecyclerView.Adapter<ListNewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    // Function to update the news list
    fun updateNews(newsList: List<NewsItem>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    // ViewHolder for the news item
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.eventName)
        private val newsPhoto: ImageView = itemView.findViewById(R.id.newsPhoto)

        fun bind(newsItem: NewsItem) {
            // Set the event name (title)
            eventName.text = newsItem.title

            // Log the image URL for debugging purposes
            Log.d("NewsItem", "Image URL: ${newsItem.image}")  // Debugging the URL

            // Check if the image URL is not empty or null
            if (!newsItem.image.isNullOrEmpty()) {
                // Load the image using Glide
                Glide.with(itemView.context)
                    .load(newsItem.image)  // Load the image URL from 'image'
                    .into(newsPhoto)  // Set the image into the ImageView
            } else {
                // Log if the image URL is empty or null
                Log.d("NewsItem", "No image URL found")  // Debug if image URL is empty
            }

            // Optional: Handle clicks or any other logic
            itemView.setOnClickListener {
                onItemClick(newsItem)
                // Handle item click if necessary
            }
        }
    }
}