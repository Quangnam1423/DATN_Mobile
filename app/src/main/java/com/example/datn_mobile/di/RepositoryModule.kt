package com.example.datn_mobile.di

import com.example.datn_mobile.data.repository.AuthRepositoryImpl
import com.example.datn_mobile.data.repository.CartRepositoryImpl
import com.example.datn_mobile.data.repository.NotificationRepositoryImpl
import com.example.datn_mobile.data.repository.ProductRepositoryImpl
import com.example.datn_mobile.data.repository.PaymentRepositoryImpl
import com.example.datn_mobile.data.repository.UserRepositoryImpl
import com.example.datn_mobile.domain.repository.AuthRepository
import com.example.datn_mobile.domain.repository.CartRepository
import com.example.datn_mobile.domain.repository.NotificationRepository
import com.example.datn_mobile.domain.repository.ProductRepository
import com.example.datn_mobile.domain.repository.UserRepository
import com.example.datn_mobile.domain.repository.PaymentRepository
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ) : CartRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ) : NotificationRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository
}