package edu.iesam.marvelapp.core.cache

import android.content.Context
import edu.iesam.marvelapp.R
import edu.iesam.marvelapp.core.time.AppTime
import kotlin.apply


interface AppCache {
    fun updateTime(key: String)
    fun isExpired(key: String, ttl: Long): Boolean
}

class AppCacheTTL(
    private val context: Context,
    private val appTime: AppTime
) : AppCache {

    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_cache), Context.MODE_PRIVATE
    )

    override fun updateTime(key: String) {
        sharedPref.edit().apply {
            putLong(key, appTime.currentTimeLong())
            apply()
        }
    }

    override fun isExpired(key: String, ttl: Long): Boolean {
        val timeSaved = sharedPref.getLong(key, -1)
        return (appTime.currentTimeLong() - timeSaved) >= ttl
    }
}