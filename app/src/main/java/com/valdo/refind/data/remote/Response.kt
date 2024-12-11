package com.valdo.refind.data.remote

import java.io.Serializable

data class TrashesResponse(
    val status: String,
    val result: List<TrashResponse>
)

data class TrashResponse(
    val ID: Int,
    val type: String,
    val treatment: String
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
