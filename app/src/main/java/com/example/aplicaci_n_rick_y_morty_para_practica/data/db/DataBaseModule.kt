package com.example.aplicaci_n_rick_y_morty_para_practica.data.di

import android.content.Context
import androidx.room.Room
import com.example.aplicaci_n_rick_y_morty_para_practica.data.db.AppDatabase
import com.example.aplicaci_n_rick_y_morty_para_practica.data.db.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rick_and_morty_db"
        ).build()
    }

    @Provides
    fun provideLogDao(appDatabase: AppDatabase): LogDao {
        return appDatabase.logDao()
    }
}