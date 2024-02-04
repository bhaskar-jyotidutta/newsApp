package com.bhaskar.newsapp.data.model


import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String
)