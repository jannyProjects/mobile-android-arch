package tech.framti.caml.di.module

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tech.framti.caml.BuildConfig
import tech.framti.data.datastore.AppPrefs
import tech.framti.data.datastore.AppPrefsImpl
import tech.framti.data.datastore.encrypted.EncryptedAppPrefs
import tech.framti.data.datastore.encrypted.EncryptedAppPrefsImpl
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(
    includes = [
        ApiModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindContext(@ApplicationContext context: Context): Context

    @Binds
    @Singleton
    abstract fun bindAppPrefs(prefs: AppPrefsImpl): AppPrefs

    @Binds
    @Singleton
    abstract fun bindEncryptedAppPrefs(prefs: EncryptedAppPrefsImpl): EncryptedAppPrefs

    companion object {

        private const val CONNECT_TIME_OUT_SECONDS = 60L
        private const val READ_TIME_OUT_SECONDS = 180L
        private const val WEB_SOCKET_PING_INTERVAL_SECONDS = 30L

        @Provides
        @Singleton
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Timber.d(message) }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }


        @Provides
        @Singleton
        @Suppress("LongParameterList")
        fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .pingInterval(WEB_SOCKET_PING_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .addNetworkInterceptor(loggingInterceptor)
                .build()

        @Provides
        @Singleton
        fun moshi(): Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        @Provides
        @Singleton
        fun provideRetrofitClient(
            okHttpClient: OkHttpClient,
            moshi: Moshi
        ): Retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .build()

    }
}
