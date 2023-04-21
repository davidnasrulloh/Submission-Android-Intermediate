package com.davidnasrulloh.sm_intermediate_david.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import androidx.datastore.preferences.core.Preferences
import com.davidnasrulloh.sm_intermediate_david.data.local.AuthPreferencesDataSource

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "application")
@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    fun provideAuthPreferences(dataStore: DataStore<Preferences>) : AuthPreferencesDataSource =
        AuthPreferencesDataSource(dataStore)
}