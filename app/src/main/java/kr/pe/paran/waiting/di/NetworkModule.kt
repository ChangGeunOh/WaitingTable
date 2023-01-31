package kr.pe.paran.waiting.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kr.pe.paran.waiting.data.repository.RemoteDataSourceImpl
import kr.pe.paran.waiting.domain.model.IpAddress
import kr.pe.paran.waiting.domain.repository.DataStoreSource
import kr.pe.paran.waiting.domain.repository.RemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideNetwork(
        dataStoreSource: DataStoreSource
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            dataStore = dataStoreSource
        )
    }
}