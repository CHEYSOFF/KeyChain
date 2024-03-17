package vk.cheysoff.keychain.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vk.cheysoff.keychain.BuildConfig
import vk.cheysoff.keychain.data.encryption.CryptoManager
import vk.cheysoff.keychain.data.local.KeychainDatabase
import vk.cheysoff.keychain.data.mappers.toDecryptedPasswordModel
import vk.cheysoff.keychain.data.mappers.toEncryptedPasswordEntity
import vk.cheysoff.keychain.domain.model.PasswordModel
import vk.cheysoff.keychain.domain.repository.KeychainRepository
import vk.cheysoff.keychain.domain.util.Resource
import javax.inject.Inject

class KeychainRepositoryImpl @Inject constructor(
    private val database: KeychainDatabase,
    private val cryptoManager: CryptoManager
) : KeychainRepository {

    override suspend fun getPasswords(): Resource<List<PasswordModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val entities = database.dao.getAllPasswords()
                Resource.Success(
                    data = entities.map { passwordEntity ->
                        passwordEntity.toDecryptedPasswordModel(
                            cryptoManager,
                            BuildConfig.CypherKey
                        )
                    }
                )
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun searchPasswords(query: String): Resource<List<PasswordModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val entities = database.dao.searchPasswords(query)
                Resource.Success(
                    data = entities.map { passwordEntity ->
                        passwordEntity.toDecryptedPasswordModel(
                            cryptoManager,
                            BuildConfig.CypherKey
                        )
                    }
                )
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun getPasswordById(id: Long): Resource<PasswordModel?> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(
                    data = database.dao
                        .getPasswordById(id)
                        ?.toDecryptedPasswordModel(cryptoManager, BuildConfig.CypherKey)
                )
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun insertPassword(passwordModel: PasswordModel): Resource<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = passwordModel.toEncryptedPasswordEntity(
                    cryptoManager,
                    BuildConfig.CypherKey
                )
                Log.d(
                    "CHEYSSSS", database.dao.insertPassword(
                        entity
                    ).toString()
                )
                Resource.Success(
                    0
                )

            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun deletePassword(id: Long): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(
                    database.dao.deletePassword(
                        id
                    )
                )
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }
    }
}