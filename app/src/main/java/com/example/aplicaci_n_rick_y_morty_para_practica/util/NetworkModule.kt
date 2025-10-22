package com.example.aplicaci_n_rick_y_morty_para_practica.util

import com.example.aplicaci_n_rick_y_morty_para_practica.data.RickApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Instalar en el ámbito de la aplicación (Singleton)
object NetworkModule {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    // 1. Instrucción para proveer Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2. Instrucción para proveer RickApiServices (usando la instancia de Retrofit)
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RickApiServices {
        return retrofit.create(RickApiServices::class.java)
    }
}