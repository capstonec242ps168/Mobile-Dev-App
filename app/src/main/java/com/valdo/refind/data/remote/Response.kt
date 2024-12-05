package com.valdo.refind.data.remote

// Generic API responses
data class TrashesResponse(
    val status: String,
    val result: List<TrashResponse>
)

data class TrashResponse(
    val ID: Int,
    val type: String,
    val treatment: String?
)

data class AuthRequest(
    val username: String,
    val password: String
)
data class AuthResponse(
    val token: String
)

data class CraftResponse(
    val ID: Int,
    val trash_id: Int,
    val craft_id: Int,
    val Crafts: CraftDetails
)

data class CraftDetails(
    val ID: Int,
    val name: String,
    val tools_materials: String,
    val step: String,
    val image: String
)

data class CraftsResponse(
    val status: String,
    val result: List<CraftResponse>?
)

data class PredictResponse(
    val result: String
)

data class BookmarkRequest(
    val itemId: String, val userId: String
)

data class BookmarkResponse(
    val success: Boolean
)

data class HistoryResponse(
    val id: String,
    val date: String,
    val action: String
)
