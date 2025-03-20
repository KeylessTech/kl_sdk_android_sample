package io.keyless.scenariodeveloperquickstart

import android.app.Application
import io.keyless.sdk.Keyless
import io.keyless.sdk.configurations.DeEnrollConfig.BiomDeEnrollConfig
import io.keyless.sdk.configurations.SetupConfig
import io.keyless.sdk.configurations.auth.BiomAuthConfig
import io.keyless.sdk.configurations.enroll.BiomEnrollConfig
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KeylessWrapper {

    /**
     * Initialize the Keyless SDK. This must be called before starting any activity calling [Keyless] apis.
     */
    fun initialize(application: Application) {
        Keyless.initialize(application)
    }

    /**
     * Setup the Keyless SDK to use the api key and host provided to you.
     */
    suspend fun setup(
        apiKey: String,
        hosts: List<String>
    ): Result<Unit> = suspendCoroutine { continuation ->

        val setupConfig = SetupConfig(apiKey = apiKey, hosts = hosts)

        Keyless.configure(setupConfig) { result ->
            when (result) {
                is Keyless.KeylessResult.Success -> {
                    continuation.resume(Result.success(result.value))
                }

                is Keyless.KeylessResult.Failure -> {
                    continuation.resume(Result.failure(result.error))
                }
            }
        }
    }

    /**
     * Check if the user is still registered on backend and also if the device was not revoked.
     */
    suspend fun isUserAndDeviceActive(): Result<Boolean> = suspendCoroutine { continuation ->
        Keyless.validateUserAndDeviceActive { result ->
            when (result) {
                is Keyless.KeylessResult.Success -> {
                    // user and device are active
                    continuation.resume(Result.success(true))
                }

                is Keyless.KeylessResult.Failure -> {
                    continuation.resume(Result.failure(result.error))
                }
            }
        }
    }

    /**
     * Enroll the user according to [BiomEnrollConfig] parameters.
     */
    suspend fun enroll(): Result<Unit> = suspendCoroutine { continuation ->

        // using default biometric enroll config
        val enrollConfig = BiomEnrollConfig()

        Keyless.enroll(enrollConfig, onCompletion = { result ->
            when (result) {
                is Keyless.KeylessResult.Success -> {
                    continuation.resume(Result.success(Unit))
                }

                is Keyless.KeylessResult.Failure -> {
                    continuation.resume(Result.failure(result.error))
                }
            }
        })
    }

    /**
     * Authenticate the user previously registered with [enroll]
     */
    suspend fun authenticate(): Result<Unit> = suspendCoroutine { continuation ->

        // adding the success animation optional screen at the end of the authentication
        val authConfig = BiomAuthConfig()

        Keyless.authenticate(authConfig, onCompletion = { result ->
            when (result) {
                is Keyless.KeylessResult.Success -> {
                    continuation.resume(Result.success(Unit))
                }

                is Keyless.KeylessResult.Failure -> {
                    continuation.resume(Result.failure(result.error))
                }
            }
        })
    }

    /**
     * Deletes the user account. Will trigger an authentication to make sure the user deleting the account is the
     * same as the one enrolled with [enroll].
     */
    suspend fun deEnroll(): Result<Unit> = suspendCoroutine { continuation ->
        val deEnrollConfig = BiomDeEnrollConfig()

        Keyless.deEnroll(deEnrollConfig, onCompletion = { result ->
            when (result) {
                is Keyless.KeylessResult.Success -> {
                    continuation.resume(Result.success(Unit))
                }

                is Keyless.KeylessResult.Failure -> {
                    continuation.resume(Result.failure(result.error))
                }
            }
        })
    }

    suspend fun reset(): Result<Unit> = suspendCoroutine { continuation ->
        Keyless.reset { result ->
            when (result) {
                is Keyless.KeylessResult.Success -> {
                    continuation.resume(Result.success(Unit))
                }

                is Keyless.KeylessResult.Failure -> {
                    continuation.resume(Result.failure(result.error))
                }
            }
        }
    }
}