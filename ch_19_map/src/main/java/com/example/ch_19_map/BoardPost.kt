package com.example.ch_19_map

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDateTime

data class BoardPost(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String, // JSON에서는 보통 String으로 받습니다.
    @SerializedName("author") val author: Author
) : Serializable

data class Author(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
) : Serializable 