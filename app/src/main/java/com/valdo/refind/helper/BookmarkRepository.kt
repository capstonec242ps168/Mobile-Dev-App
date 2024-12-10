package com.valdo.refind.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.valdo.refind.data.remote.CraftDetails
import com.valdo.refind.data.remote.CraftResponse

object BookmarkRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "BookmarkRepository"

    fun addCraftToBookmarks(craft: CraftResponse, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = getCurrentUserId()
        if (userId != null) {
            val bookmarkData = hashMapOf(
                "craft_id" to craft.craft_id,
                "name" to craft.Crafts.name,
                "image" to craft.Crafts.image,
                "tools_materials" to craft.Crafts.tools_materials,
                "step" to craft.Crafts.step
            )

            firestore.collection("users")
                .document(userId)
                .collection("bookmarks")
                .document(craft.craft_id.toString())
                .set(bookmarkData)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }

    fun getBookmarkedCrafts(onResult: (List<CraftResponse>) -> Unit, onFailure: (Exception) -> Unit) {
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
                    onFailure(e)
                }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }

    fun removeCraftFromBookmarks(craft: CraftResponse, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = getCurrentUserId()
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("bookmarks")
                .document(craft.craft_id.toString())
                .delete()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
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

