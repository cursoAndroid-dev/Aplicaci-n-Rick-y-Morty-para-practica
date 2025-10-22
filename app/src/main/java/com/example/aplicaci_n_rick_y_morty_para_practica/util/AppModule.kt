package com.example.aplicaci_n_rick_y_morty_para_practica.util

import android.content.Context
import com.example.aplicaci_n_rick_y_morty_para_practica.util.FavoriteManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFavoriteManager(@ApplicationContext context: Context): FavoriteManager {
        return FavoriteManager(context)
    }
}