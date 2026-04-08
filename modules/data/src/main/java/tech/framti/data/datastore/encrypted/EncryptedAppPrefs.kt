package tech.framti.data.datastore.encrypted

import kotlinx.coroutines.flow.Flow
import tech.framti.data.datastore.encrypted.serializer.AuthPreferences

interface EncryptedAppPrefs {
    val authPreferences: Flow<AuthPreferences>
    suspend fun setAuthPreferences(userPreferences: AuthPreferences)
}