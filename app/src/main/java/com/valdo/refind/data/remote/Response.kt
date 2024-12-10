package com.valdo.refind.data.remote

import java.io.Serializable

// Generic API responses
data class TrashesResponse(
    val status: String,
    val result: List<TrashResponse>
)

data class TrashResponse(
    val ID: Int,
    val type: String,
    val treatment: String
)

data class AuthRequest(
    val username: String,
    val password: String
)
data class AuthResponse(
    val token: String
)

data class CraftResponse(
    val ID: Int = 0,
    val trash_id: Int = 0,
    val craft_id: Int = 0,
    val Crafts: CraftDetails
)

data class CraftDetails(
    val ID: Int = 0,
    val name: String,
    val tools_materials: String,
    val step: String,
    val image: String
)


data class CraftsResponse(
    val status: String,
    val result: List<CraftResponse>
)

data class PredictResponse(
    val result: String,
    val message: String,
    val data: PredictionData
)

data class PredictionData(
    val id_trash: Int,
    val result: String
)

data class BookmarkRequest(
    val itemId: String,
    val userId: String
)

data class BookmarkResponse(
    val status: String,
    val message: String
)

data class HistoryResponse(
    val id: String,
    val date: String,
    val action: String
)

data class NewsResponse(
    val status: String,
    val result: List<NewsItem>
)

data class NewsItem(
    val id: Int,
    val title: String,
    val content: String,
    val image: String
) : Serializable
