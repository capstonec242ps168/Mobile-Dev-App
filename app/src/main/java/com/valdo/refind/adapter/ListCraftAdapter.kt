package com.valdo.refind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.valdo.refind.R
import com.valdo.refind.data.remote.CraftResponse

class ListCraftAdapter(
    private val context: Context,
    private val onItemClick: (CraftResponse, ImageView, TextView) -> Unit,
    private val onBookmarkClick: (CraftResponse) -> Unit
) : RecyclerView.Adapter<ListCraftAdapter.CraftViewHolder>() {

    private var craftList: List<CraftResponse> = emptyList()
    private val bookmarkedItems = mutableSetOf<Int>()

    private val sharedPreferences = context.getSharedPreferences("bookmarks_prefs", Context.MODE_PRIVATE)

    init {
        loadBookmarkedItems()
    }

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
        private val btnBookmark: ImageButton = itemView.findViewById(R.id.btnBookmark)

        fun bind(craft: CraftResponse) {
            // Set the craft title
            craftTitle.text = craft.Crafts?.name

            // Load the craft image using Glide
            if (!craft.Crafts?.image.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(craft.Crafts?.image)
                    .into(craftImage)
            }

            // Set the bookmark icon based on the bookmark state
            btnBookmark.setImageResource(
                if (bookmarkedItems.contains(craft.craft_id)) {
                    R.drawable.baseline_bookmark_24
                } else {
                    R.drawable.baseline_bookmark_border_24
                }
            )

            // Set transition names for shared elements
            craftImage.transitionName = "craftImageTransition_${craft.craft_id}"
            craftTitle.transitionName = "craftTitleTransition_${craft.craft_id}"

            // Handle item click and pass shared elements
            itemView.setOnClickListener {
                onItemClick(craft, craftImage, craftTitle)
            }

            // Handle bookmark button click
            btnBookmark.setOnClickListener {
                if (bookmarkedItems.contains(craft.craft_id)) {
                    // If already bookmarked, remove it
                    bookmarkedItems.remove(craft.craft_id)
                    btnBookmark.setImageResource(R.drawable.baseline_bookmark_border_24)  // Bookmark removed
                    onBookmarkClick(craft)  // Notify to remove from BookmarkFragment
                } else {
                    // If not bookmarked, add it
                    bookmarkedItems.add(craft.craft_id)
                    btnBookmark.setImageResource(R.drawable.baseline_bookmark_24)  // Bookmark added
                    onBookmarkClick(craft)  // Notify to add to BookmarkFragment
                }
                saveBookmarkedItems()
            }

        }
    }

    // Save bookmark states to SharedPreferences
    private fun saveBookmarkedItems() {
        with(sharedPreferences.edit()) {
            putStringSet("bookmarked_items", bookmarkedItems.map { it.toString() }.toSet())
            apply()
        }
    }

    // Load bookmark states from SharedPreferences
    private fun loadBookmarkedItems() {
        val savedBookmarks = sharedPreferences.getStringSet("bookmarked_items", emptySet())
        bookmarkedItems.clear()
        bookmarkedItems.addAll(savedBookmarks?.map { it.toInt() } ?: emptySet())
    }
}

