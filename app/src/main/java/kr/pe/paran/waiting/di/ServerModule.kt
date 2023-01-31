package kr.pe.paran.waiting.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.ktor.server.Server
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerModule {

    @Provides
    @Singleton
    fun provideServer(
        context: Context,
        usesCases: UsesCases
    ): Server {
        return Server(context = context, usesCases = usesCases)
    }
}