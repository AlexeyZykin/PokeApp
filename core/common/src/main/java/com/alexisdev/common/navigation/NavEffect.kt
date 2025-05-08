package com.alexisdev.common.navigation

sealed interface NavEffect {
    data class NavigateTo(val direction: NavDirection) : NavEffect

    data object NavigateUp : NavEffect
}