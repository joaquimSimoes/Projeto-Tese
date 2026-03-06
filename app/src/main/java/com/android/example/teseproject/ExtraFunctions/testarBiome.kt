package com.android.example.teseproject.ExtraFunctions

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager

fun logBiometricStatus(context: Context) {
    val bioManager = BiometricManager.from(context)

    val strong = bioManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    val weak = bioManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
    val deviceCredential = bioManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)

    Log.d("BiometricDebug", "==============================")
    Log.d("BiometricDebug", "Device: ${Build.MANUFACTURER} ${Build.MODEL}")
    Log.d("BiometricDebug", "Android version: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
    Log.d("BiometricDebug", "BIOMETRIC_STRONG -> $strong")
    Log.d("BiometricDebug", "BIOMETRIC_WEAK -> $weak")
    Log.d("BiometricDebug", "DEVICE_CREDENTIAL -> $deviceCredential")
    Log.d("BiometricDebug", "==============================")

    when (strong) {
        BiometricManager.BIOMETRIC_SUCCESS -> Log.d("BiometricDebug", "✅ Strong biometric available (fingerprint or 3D face)")
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.d("BiometricDebug", "⚠️ No strong biometric enrolled")
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.d("BiometricDebug", "❌ No strong biometric hardware")
    }
    when (weak) {
        BiometricManager.BIOMETRIC_SUCCESS -> Log.d("BiometricDebug", "✅ Weak biometric available (e.g., 2D face)")
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.d("BiometricDebug", "⚠️ No weak biometric enrolled")
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.d("BiometricDebug", "❌ No weak biometric hardware")
    }
}


fun logAllAuthenticators(context: Context) {
    val bioManager = BiometricManager.from(context)

    val all = bioManager.canAuthenticate(
        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
    )

    Log.d("BiometricDebug", "Combined (STRONG | WEAK | DEVICE_CREDENTIAL) -> $all")

    when (all) {
        BiometricManager.BIOMETRIC_SUCCESS ->
            Log.d("BiometricDebug", "✅ Can authenticate with some biometric or credential")
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
            Log.d("BiometricDebug", "⚠️ Device supports biometrics but none are enrolled")
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
            Log.d("BiometricDebug", "❌ No biometric hardware reported")
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
            Log.d("BiometricDebug", "⚠️ Hardware temporarily unavailable")
        else ->
            Log.d("BiometricDebug", "❓ Unknown code: $all")
    }
}

fun checkEnrolledBiometrics(context: Context) {
    val biometricManager = BiometricManager.from(context)

    val strong = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    val weak = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

    Log.d("BiometricDebug", "Strong biometric: $strong")
    Log.d("BiometricDebug", "Weak biometric: $weak")
}

fun logEnrolledBiometrics(context: Context) {
    val enrolled = Settings.Secure.getString(context.contentResolver, "face_unlock_keyguard_enabled")
    Log.d("BiometricDebug", "Face unlock keyguard enabled: $enrolled")
}