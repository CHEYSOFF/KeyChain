package vk.cheysoff.keychain.di

import android.content.Context
import androidx.room.Room
import com.lambdapioneer.argon2kt.Argon2Kt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import vk.cheysoff.keychain.BuildConfig
import vk.cheysoff.keychain.data.biometric.BiometricAuthenticator
import vk.cheysoff.keychain.data.encryption.CryptoManager
import vk.cheysoff.keychain.data.encryption.HashingManager
import vk.cheysoff.keychain.data.local.KeychainDatabase
import vk.cheysoff.keychain.data.local.SQLCipherHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideShopDatabase(@ApplicationContext context: Context): KeychainDatabase {
        val password = BuildConfig.DatabaseCypher
        val passphrase: ByteArray = SQLiteDatabase.getBytes(password.toCharArray())
        val factory = SQLCipherHelperFactory(passphrase)
        return Room
            .databaseBuilder(context, KeychainDatabase::class.java, "keychainDB.db")
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideBiometricAuthenticator(@ApplicationContext context: Context): BiometricAuthenticator {
        return BiometricAuthenticator(context)
    }

    @Provides
    @Singleton
    fun provideArgon(): Argon2Kt {
        return Argon2Kt()
    }

    @Provides
    @Singleton
    fun provideHashingManager(argon2Kt: Argon2Kt): HashingManager {
        return HashingManager(argon2Kt)
    }

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager {
        return CryptoManager()
    }
}