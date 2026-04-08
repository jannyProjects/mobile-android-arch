package tech.framti.data.datastore

import kotlinx.coroutines.flow.Flow

interface AppPrefs {
    val counter: Flow<Int>
    suspend fun setCounter(mode: Int)
}