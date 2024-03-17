package vk.cheysoff.keychain.data.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class BiometricAuthenticator @Inject constructor(
    private val appContext: Context
) {


    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val biometricManager = BiometricManager.from(appContext.applicationContext)

    private lateinit var biometricPrompt: BiometricPrompt

    fun isBiometricAuthAvailable(): BiometricAuthenticationStatus {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthenticationStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthenticationStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthenticationStatus.TEMPORARY_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthenticationStatus.NOT_AVAILABLE
        }
    }

    /**
     * @param onFailed called when the Fingerprint or faceId is presented but the verification fails.
     * @param onError called when the system cannot display the Fingerprint/faceId dialog.
     */
    fun promptBiometricAuth(
        title: String,
        subTitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        when(isBiometricAuthAvailable()) {
            BiometricAuthenticationStatus.NOT_AVAILABLE -> {
                onError(BiometricAuthenticationStatus.NOT_AVAILABLE.code, "Not available for this device")
                return
            }
            BiometricAuthenticationStatus.TEMPORARY_NOT_AVAILABLE -> {
                onError(BiometricAuthenticationStatus.TEMPORARY_NOT_AVAILABLE.code, "Not available at this moment")
                return
            }
            BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                onError(BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED.code, "You should add a fingerprint or face id first")
                return
            }
            else -> Unit
        }

        biometricPrompt =
            BiometricPrompt(
                fragmentActivity,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess(result)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onError(errorCode, errString)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailed()
                    }
                }
            )
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

}