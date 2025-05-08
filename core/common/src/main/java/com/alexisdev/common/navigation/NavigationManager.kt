package com.alexisdev.common.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager {
    private val _navEffectFlow = MutableSharedFlow<NavEffect>(extraBufferCapacity = 1)
    val navEffectFlow: SharedFlow<NavEffect> = _navEffectFlow.asSharedFlow()

    fun navigate(navEffect: NavEffect) {
        _navEffectFlow.tryEmit(navEffect)
    }
}