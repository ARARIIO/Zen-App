package zentimer.app.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserPreferences(
    val focusDurationMinutes: Int,
    val shortBreakMinutes: Int,
    val longBreakMinutes: Int,
    val backgroundSound: String,
    val hapticFeedbackEnabled: Boolean
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val FOCUS_DURATION = intPreferencesKey("focus_duration_minutes")
        private val SHORT_BREAK = intPreferencesKey("short_break_minutes")
        private val LONG_BREAK = intPreferencesKey("long_break_minutes")
        private val BACKGROUND_SOUND = stringPreferencesKey("background_sound")
        private val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback_enabled")

        const val DEFAULT_FOCUS = 25
        const val DEFAULT_SHORT_BREAK = 5
        const val DEFAULT_LONG_BREAK = 15
        const val SOUND_NONE = "none"
        const val SOUND_NATURE = "nature"
        const val SOUND_SPACE = "space"
        const val SOUND_WHITE_NOISE = "white_noise"
    }

    val preferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            focusDurationMinutes = prefs[FOCUS_DURATION] ?: DEFAULT_FOCUS,
            shortBreakMinutes = prefs[SHORT_BREAK] ?: DEFAULT_SHORT_BREAK,
            longBreakMinutes = prefs[LONG_BREAK] ?: DEFAULT_LONG_BREAK,
            backgroundSound = prefs[BACKGROUND_SOUND] ?: SOUND_NONE,
            hapticFeedbackEnabled = prefs[HAPTIC_FEEDBACK] ?: true
        )
    }

    suspend fun setFocusDuration(minutes: Int) {
        dataStore.edit { it[FOCUS_DURATION] = minutes }
    }

    suspend fun setShortBreak(minutes: Int) {
        dataStore.edit { it[SHORT_BREAK] = minutes }
    }

    suspend fun setLongBreak(minutes: Int) {
        dataStore.edit { it[LONG_BREAK] = minutes }
    }

    suspend fun setBackgroundSound(sound: String) {
        dataStore.edit { it[BACKGROUND_SOUND] = sound }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        dataStore.edit { it[HAPTIC_FEEDBACK] = enabled }
    }
}
