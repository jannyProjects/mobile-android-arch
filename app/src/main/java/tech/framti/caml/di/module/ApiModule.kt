package tech.framti.caml.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import tech.framti.api.card.ScratchCardService
import tech.framti.data.api.card.ScratchCardApi
import tech.framti.data.api.card.ScratchCardApiImpl

@Module()
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    companion object {
        @Provides
        fun provideCardService(retrofit: Retrofit): ScratchCardService =
            retrofit.create(ScratchCardService::class.java)
    }

    @Binds
    abstract fun bindScratchCardApi(api: ScratchCardApiImpl): ScratchCardApi

}
