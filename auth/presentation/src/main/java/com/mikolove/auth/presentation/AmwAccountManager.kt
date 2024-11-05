package com.mikolove.auth.presentation

import android.app.Activity
import com.mikolove.auth.domain.CredentialError
import com.mikolove.auth.domain.CredentialInfo
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result

class AmwAccountManager(
    private val activity: Activity
) {
    private val credentialManager =
        androidx.credentials.CredentialManager.Companion.create(activity)

    suspend fun saveCredential(username: String, password: String): EmptyResult<DataError> {
        return try {
            credentialManager.createCredential(
                context = activity,
                request = androidx.credentials.CreatePasswordRequest(
                    id = username,
                    password = password
                )
            )
            Result.Success(Unit)
        } catch (e: androidx.credentials.exceptions.CreateCredentialCancellationException) {
            e.printStackTrace()
            Result.Error(CredentialError.CREATE_CANCELATION)
        } catch(e: androidx.credentials.exceptions.CreateCredentialException) {
            e.printStackTrace()
            Result.Error(CredentialError.CREATE_EXCEPTION)
        }
    }

    suspend fun loadCredential(): Result<CredentialInfo, DataError> {
        return try {
            val credentialResponse = credentialManager.getCredential(
                context = activity,
                request = androidx.credentials.GetCredentialRequest(
                    credentialOptions = listOf(androidx.credentials.GetPasswordOption())
                )
            )

            val credential = credentialResponse.credential as? androidx.credentials.PasswordCredential
                ?: return Result.Error(CredentialError.GET_EXCEPTION)

            Result.Success(CredentialInfo(credential.id, credential.password))

        } catch(e: androidx.credentials.exceptions.GetCredentialCancellationException) {
            e.printStackTrace()
            return Result.Error(CredentialError.GET_CANCELATION)
        } catch(e: androidx.credentials.exceptions.NoCredentialException) {
            e.printStackTrace()
            return Result.Error(CredentialError.NOT_EXIST)
        } catch(e: androidx.credentials.exceptions.GetCredentialException) {
            e.printStackTrace()
            Result.Error(CredentialError.GET_EXCEPTION)
        }
    }
}