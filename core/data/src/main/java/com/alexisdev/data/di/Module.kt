package com.alexisdev.data.di

import android.content.Context
import androidx.room.Room
import com.alexisdev.data.repo.PokeRepoImpl
import com.alexisdev.data.room.db.PokeDatabase
import com.alexisdev.domain.repo.PokeRepo
import org.koin.dsl.module

val dataModule = module {
    single<PokeRepo> { PokeRepoImpl(get(), get()) }
    single { provideRoomDb(get()) }
    single { providePokeDao(get()) }
}

private fun provideRoomDb(context: Context): PokeDatabase {
    return Room.databaseBuilder(
        context,
        PokeDatabase::class.java,
        PokeDatabase.DATABASE_NAME
    ).build()
}

private fun providePokeDao(pokeDb: PokeDatabase) = pokeDb.getPokeDao()