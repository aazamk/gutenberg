package com.example.mybook.service

import com.example.mybook.model.Results
import com.google.gson.annotations.SerializedName


/**
 * @author Azam Khan
 */
data class BookResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val data: List<Results>?
)