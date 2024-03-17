package vk.cheysoff.keychain.domain.repository

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import vk.cheysoff.keychain.domain.model.AppDataModel
import vk.cheysoff.keychain.domain.util.Resource

interface AppDataRepository {
    suspend fun getAppData(): Resource<AppDataModel?>
    suspend fun upsertAppData(appData: AppDataModel)
    suspend fun verifyPassword(password: String): Resource<Boolean>
    fun useBiometrics(
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (id: Int, message: CharSequence) -> Unit,
    )
    suspend fun updateBiometricUsage(isBiometricEnabled: Boolean)
    suspend fun updateMasterKeyHash(newMasterKey: String)
}