package com.alexisdev.common.di

import com.alexisdev.common.navigation.NavigationManager
import org.koin.dsl.module

val commonModule = module {
    single { NavigationManager() }
}