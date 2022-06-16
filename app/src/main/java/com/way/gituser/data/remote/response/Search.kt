package com.way.gituser.data.remote.response

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("total_count")
    val totalCount: Int
)