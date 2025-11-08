package com.example.datn_mobile.di

import com.example.datn_mobile.data.network.api.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) : AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}