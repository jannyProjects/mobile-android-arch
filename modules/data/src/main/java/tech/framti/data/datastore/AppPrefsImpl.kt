package tech.framti.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class AppPrefsImpl @Inject internal constructor(
    private val context: Context
) : AppPrefs {

    private val settingsDataStore = PreferenceDataStoreFactory.create {
        File(context.filesDir, "datastore/settings.preferences_pb")
    }

    override suspend fun setCounter(mode: Int) {
        settingsDataStore.edit { settings ->
            settings[EXAMPLE_COUNTER] = mode
        }
    }

    override val counter: Flow<Int> = settingsDataStore.data.map { preferences ->
        preferences[EXAMPLE_COUNTER] ?: 0
    }

    private companion object {
        private val EXAMPLE_COUNTER = intPreferencesKey("example_counter")
    }
}