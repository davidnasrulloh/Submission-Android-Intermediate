package com.davidnasrulloh.sm_intermediate_david.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

class AuthPreferencesDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object{
        private val TOKEN_KEY = stringPreferencesKey("token_data")
    }

    fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
}