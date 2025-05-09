package com.alexisdev.pokeapp

import android.app.Application
import com.alexisdev.common.di.commonModule
import com.alexisdev.data.di.dataModule
import com.alexisdev.domain.di.domainModule
import com.alexisdev.poke_api.pokeApiModule
import com.alexisdev.pokemon_details.di.pokemonDetailsFeatureModule
import com.alexisdev.pokemon_main.di.pokemonMainFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                pokeApiModule,
                dataModule,
                domainModule,
                commonModule,
                pokemonMainFeatureModule,
                pokemonDetailsFeatureModule
            )
        }
    }
}