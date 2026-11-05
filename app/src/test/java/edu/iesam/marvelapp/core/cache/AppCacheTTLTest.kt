package edu.iesam.marvelapp.core.cache

import android.content.Context
import android.content.SharedPreferences
import edu.iesam.marvelapp.core.time.AppTime
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class AppCacheTTLTest {

    private val context: Context = mockk()
    private val sharedPref: SharedPreferences = mockk()
    private val editorf: SharedPreferences.Editor = mockk(relaxed = true)
    private val appTime: AppTime = mockk()

    private lateinit var appCache: AppCacheTTL

    @Before

    fun setUp() {
        every { context.getString(any()) } returns "app_cache"
        every { context.getSharedPreferences("app_cache", Context.MODE_PRIVATE) } returns sharedPref
        every { sharedPref.edit() } returns editorf
        every { editorf.putLong(any(), any()) } returns editorf

        appCache = AppCacheTTL(context, appTime)
    }

    @Test
    fun `updateTime guarda la hora actual con la clave indicada`() {

        // Given
        every { appTime.currentTimeLong() } returns 1_000L

        // When
        appCache.updateTime("heroes")

        // Then
        verify(exactly = 1) { editorf.putLong("heroes", 1_000L) }
        verify(exactly = 1) { editorf.apply() }

    }

    @Test
    fun `isExpired devuelve true si la clave nunca se ha guardado`() {
        // Given
        every { sharedPref.getLong("heroes", -1) } returns -1L
        every { appTime.currentTimeLong() } returns 5_000L

        // When
        val result = appCache.isExpired("heroes", ttl = 1_000L)

        // Then
        assertTrue(result)
    }


}