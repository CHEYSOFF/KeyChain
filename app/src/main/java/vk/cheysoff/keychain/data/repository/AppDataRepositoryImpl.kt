package vk.cheysoff.keychain.data.repository

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vk.cheysoff.keychain.BuildConfig
import vk.cheysoff.keychain.data.biometric.BiometricAuthenticator
import vk.cheysoff.keychain.data.encryption.HashingManager
import vk.cheysoff.keychain.data.local.KeychainDatabase
import vk.cheysoff.keychain.data.mappers.toAppDataModel
import vk.cheysoff.keychain.data.mappers.toHashedAppDataEntity
import vk.cheysoff.keychain.data.mappers.toHashedPassword
import vk.cheysoff.keychain.domain.model.AppDataModel
import vk.cheysoff.keychain.domain.repository.AppDataRepository
import vk.cheysoff.keychain.domain.util.Resource
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val database: KeychainDatabase,
    private val hashingManager: HashingManager,
    private val biometricAuthenticator: BiometricAuthenticator
) : AppDataRepository {
    override suspend fun getAppData(): Resource<AppDataModel?> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(
                    data = database.dao.getAppData()?.toAppDataModel()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun upsertAppData(appData: AppDataModel) {
        withContext(Dispatchers.IO) {
            database.dao.insertAppData(
                appData.toHashedAppDataEntity(
                    hashingManager,
                    BuildConfig.Salt
                )
            )
        }
    }

    override suspend fun verifyPassword(password: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val hashedPassword = database.dao.getAppData()
                    ?: throw NullPointerException("Master key was not saved")

                Resource.Success(
                    data = hashingManager.verifyPassword(hashedPassword.hashedMasterKey, password)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    override fun useBiometrics(
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (id: Int, message: CharSequence) -> Unit
    ) {
        biometricAuthenticator.promptBiometricAuth(
            title = "Access your passwords",
            subTitle = "Use your finger print or face id",
            negativeButtonText = "Cancel",
            fragmentActivity = fragmentActivity,
            onSuccess = onSuccess,
            onFailed = onFailed,
            onError = onError,

            )
    }

    override suspend fun updateBiometricUsage(isBiometricEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            database.dao.updateBiometricUsage(isBiometricEnabled)
        }
    }

    override suspend fun updateMasterKeyHash(newMasterKey: String) {
        withContext(Dispatchers.IO) {
            database.dao.updateMasterKeyHash(
                newMasterKey.toHashedPassword(
                    hashingManager,
                    BuildConfig.Salt
                )
            )
        }
    }


    companion object {
        const val password = BuildConfig.CypherKey
    }
}