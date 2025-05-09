package com.alexisdev.data.utils

object PokeUtils {

    fun generateRandomOffsetValue(upperBound: Int, pageSize: Int) : Int {
        return (0..(upperBound - pageSize).coerceAtLeast(0)).random()
    }
}