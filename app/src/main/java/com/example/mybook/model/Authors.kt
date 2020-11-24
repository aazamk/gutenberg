package com.example.mybook.model

import com.google.gson.annotations.SerializedName
/**
 * @author Azam Khan
 */
data class Author(
    @SerializedName("name") val name: String,
    @SerializedName("birth_year") val birth_year: Int,
    @SerializedName("death_year") val death_year: Int
)