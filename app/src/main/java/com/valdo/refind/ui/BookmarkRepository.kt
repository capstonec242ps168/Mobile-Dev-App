package com.valdo.refind.ui

import com.google.firebase.auth.FirebaseAuth
import com.valdo.refind.data.remote.CraftResponse

object BookmarkRepository {
    private val userBookmarks = mutableMapOf<String, MutableList<CraftResponse>>()

    fun addCraftToBookmarks(craft: CraftResponse) {
        val userId = getCurrentUserId()
        if (userId != null) {
            val bookmarks = userBookmarks.getOrPut(userId) { mutableListOf() }
            if (!bookmarks.contains(craft)) {
                bookmarks.add(craft)
            }
        }
    }

    fun getBookmarkedCrafts(): List<CraftResponse> {
        val userId = getCurrentUserId()
        return userBookmarks[userId] ?: emptyList()
    }

    fun removeCraftFromBookmarks(craft: CraftResponse) {
        val userId = getCurrentUserId()
        userBookmarks[userId]?.remove(craft)
    }

    fun clearBookmarksForUser() {
        val userId = getCurrentUserId()
        userBookmarks[userId]?.clear()
    }

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}
