package com.example.datn_mobile.di

import com.example.datn_mobile.data.repository.AuthRepositoryImpl
import com.example.datn_mobile.data.repository.ProductRepositoryImpl
import com.example.datn_mobile.domain.repository.AuthRepository
import com.example.datn_mobile.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ) : ProductRepository
}