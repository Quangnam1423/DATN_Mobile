package com.example.datn_mobile.di

import android.content.Context
import com.example.datn_mobile.data.local.PreferenceDataSource
import com.example.datn_mobile.data.repository.UserPreferencesRepositoryImpl
import com.example.datn_mobile.domain.repository.UserPreferencesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(userPreferencesRepository: UserPreferencesRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val token = runBlocking { userPreferencesRepository.authToken.firstOrNull() }
                val request = chain.request().newBuilder()
                if (token != null) {
                    request.addHeader("Authorization", "Bearer $token")
                }
                val response = chain.proceed(request.build())
                if (response.code >= 500) {
                    runBlocking {
                        userPreferencesRepository.clearAll()
                    }
                }
                response
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.102.7:8080")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(preferenceDataSource: PreferenceDataSource): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(preferenceDataSource)
    }
}
