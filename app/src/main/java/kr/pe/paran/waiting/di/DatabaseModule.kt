package kr.pe.paran.waiting.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.data.local.database.LocalDatabase
import kr.pe.paran.waiting.data.repository.LocalDataSourceImpl
import kr.pe.paran.waiting.domain.repository.LocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): LocalDatabase {
        return Room
            .databaseBuilder(
                context,
                LocalDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(
        database: LocalDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(database)
    }
}

/*
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BorutoDatabase {
        return Room.databaseBuilder(
            context,
            BorutoDatabase::class.java,
            BORUTO_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: BorutoDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(
            borutoDatabase = database
        )
    }
 */