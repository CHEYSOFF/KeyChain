package vk.cheysoff.keychain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vk.cheysoff.keychain.data.repository.AppDataRepositoryImpl
import vk.cheysoff.keychain.data.repository.KeychainRepositoryImpl
import vk.cheysoff.keychain.domain.repository.AppDataRepository
import vk.cheysoff.keychain.domain.repository.KeychainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAppDataRepository(
        shopRepositoryImpl: AppDataRepositoryImpl
    ): AppDataRepository

    @Binds
    @Singleton
    abstract fun provideKeychainRepository(
        keychainRepositoryImpl: KeychainRepositoryImpl
    ): KeychainRepository
}