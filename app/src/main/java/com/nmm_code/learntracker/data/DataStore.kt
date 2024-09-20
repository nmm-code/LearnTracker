package com.nmm_code.learntracker.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "state")


class DataStoreState<T>(private val context: Context, private val key: Preferences.Key<T>) {

    companion object {
        val PAGE = intPreferencesKey("page")
        val SELECT_INFO = booleanPreferencesKey("select_info")
        val PATH = stringPreferencesKey("path")
        val TODO_ID = intPreferencesKey("todo_id")
    }

    suspend fun get(default: T): T {
        val flow: Flow<T> = context.dataStore.data
            .map { pref ->
                pref[key] ?: default
            }
        return flow.first()
    }

    suspend fun set(state: T) {
        context.dataStore.edit { pref ->
            pref[key] = state
        }
    }

    @Composable
    fun getAsState(default: T) = context.dataStore.data
        .map { pref ->
            pref[key] ?: default
        }.collectAsState(initial = default)
}