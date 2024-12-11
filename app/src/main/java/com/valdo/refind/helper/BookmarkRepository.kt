package com.valdo.refind.helper

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.valdo.refind.data.remote.CraftDetails
import com.valdo.refind.data.remote.CraftResponse

object BookmarkRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "BookmarkRepository"

    fun toggleBookmark(
        craft: CraftResponse,
        onSuccess: (isAdded: Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = getCurrentUserId()
        if (userId != null) {
            val bookmarkRef = firestore.collection("users")
                .document(userId)
                .collection("bookmarks")
                .document(craft.craft_id.toString())

            bookmarkRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // If the bookmark exists, remove it
                        bookmarkRef.delete()
                            .addOnSuccessListener {
                                Log.d(TAG, "Craft removed from bookmarks: ${craft.craft_id}")
                                onSuccess(false) // Indicate the bookmark was removed
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error removing bookmark: ${e.message}", e)
                                onFailure(e)
                            }
                    } else {
                        // If the bookmark does not exist, add it
                        val bookmarkData = hashMapOf(
                            "craft_id" to craft.craft_id,
                            "name" to craft.Crafts?.name,
                            "image" to craft.Crafts?.image,
                            "tools_materials" to craft.Crafts?.tools_materials,
                            "step" to craft.Crafts?.step
                        )

                        bookmarkRef.set(bookmarkData)
                            .addOnSuccessListener {
                                Log.d(TAG, "Craft added to bookmarks: ${craft.craft_id}")
                                onSuccess(true) // Indicate the bookmark was added
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error adding bookmark: ${e.message}", e)
                                onFailure(e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error checking bookmark existence: ${e.message}", e)
                    onFailure(e)
                }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }

    fun getBookmarkedCrafts(
        onResult: (List<CraftResponse>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = getCurrentUserId()
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("bookmarks")
                .get()
                .addOnSuccessListener { documents ->
                    val bookmarks = documents.map { doc ->
                        CraftResponse(
                            craft_id = doc.getLong("craft_id")?.toInt() ?: 0,
                            Crafts = CraftDetails(
                                name = doc.getString("name") ?: "",
                                image = doc.getString("image") ?: "",
                                tools_materials = doc.getString("tools_materials") ?: "",
                                step = doc.getString("step") ?: ""
                            )
                        )
                    }
                    onResult(bookmarks)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error fetching bookmarks: ${e.message}", e)
                    onFailure(e)
                }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}


