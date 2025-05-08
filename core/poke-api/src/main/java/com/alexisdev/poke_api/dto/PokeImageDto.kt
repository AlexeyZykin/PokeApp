package com.alexisdev.poke_api.dto

import com.google.gson.annotations.SerializedName

data class PokeImageDto(
    @SerializedName("front_default")
    val frontDefault: String?
)
