package tech.framti.data.datastore.encrypted

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import kotlinx.coroutines.flow.Flow
import tech.framti.data.datastore.encrypted.serializer.AuthPreferences
import tech.framti.data.datastore.encrypted.serializer.AuthPreferencesSerializer
import java.io.File
import javax.inject.Inject

class EncryptedAppPrefsImpl @Inject internal constructor(
    private val context: Context
) : EncryptedAppPrefs {

    private val encryptedDataStore = DataStoreFactory.create(
        serializer = AuthPreferencesSerializer
    ) {
        File(context.filesDir, "datastore/encryptedSettings.preferences_pb")
    }

    override suspend fun setAuthPreferences(userPreferences: AuthPreferences) {
        encryptedDataStore.updateData { prefs ->
            userPreferences
        }
    }

    override val authPreferences: Flow<AuthPreferences> = encryptedDataStore.data
}