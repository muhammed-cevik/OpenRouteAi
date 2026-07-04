package com.tdev.openrai.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "openrouteai_prefs")

class AppPrefs(private val context: Context) {

    private val API_KEY_KEY   = stringPreferencesKey("api_key")
    private val MODEL_KEY     = stringPreferencesKey("selected_model")

    val apiKey: Flow<String> = context.dataStore.data.map { it[API_KEY_KEY] ?: "" }
    val selectedModel: Flow<String> = context.dataStore.data.map {
        it[MODEL_KEY] ?: "meta-llama/llama-3.3-70b-instruct:free"
    }

    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { it[API_KEY_KEY] = key.trim() }
    }

    suspend fun saveModel(modelId: String) {
        context.dataStore.edit { it[MODEL_KEY] = modelId }
    }
}
