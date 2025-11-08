package com.example.datn_mobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object RecentSearchManager {
    private val Context.dataStore by preferencesDataStore(name = "recent_searchs")
    private val RECENT_SEARCH_KEY = stringSetPreferencesKey("search_history")

    suspend fun saveSearch(context: Context, keyword: String) {
        context.dataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCH_KEY]?.toMutableSet()?: mutableSetOf()
            currentSearches.remove(keyword)
            currentSearches.add(keyword)
            preferences[RECENT_SEARCH_KEY] = currentSearches
        }
    }

    /**
     *
     */
    suspend fun getRecentSearches(context: Context) : List<String> {
        return context.dataStore.data.map { preferences ->
            preferences[RECENT_SEARCH_KEY]?.toList() ?: emptyList()
        }.first()
    }

    /**
     * clear cache search
     */
    suspend fun clearSearches(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCH_KEY)
        }
    }
}