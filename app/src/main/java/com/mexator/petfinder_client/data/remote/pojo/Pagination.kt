package com.mexator.petfinder_client.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("count_per_page")
    val countPerPage: Int,
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)