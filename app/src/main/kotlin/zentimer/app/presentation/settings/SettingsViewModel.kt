package zentimer.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import zentimer.app.data.preferences.UserPreferences
import zentimer.app.data.preferences.UserPreferencesRepository

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences(
            focusDurationMinutes = UserPreferencesRepository.DEFAULT_FOCUS,
            shortBreakMinutes = UserPreferencesRepository.DEFAULT_SHORT_BREAK,
            longBreakMinutes = UserPreferencesRepository.DEFAULT_LONG_BREAK,
            backgroundSound = UserPreferencesRepository.SOUND_NONE,
            hapticFeedbackEnabled = true
        ))

    fun setFocusDuration(minutes: Int) {
        viewModelScope.launch { preferencesRepository.setFocusDuration(minutes) }
    }

    fun setShortBreak(minutes: Int) {
        viewModelScope.launch { preferencesRepository.setShortBreak(minutes) }
    }

    fun setLongBreak(minutes: Int) {
        viewModelScope.launch { preferencesRepository.setLongBreak(minutes) }
    }

    fun setBackgroundSound(sound: String) {
        viewModelScope.launch { preferencesRepository.setBackgroundSound(sound) }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch { preferencesRepository.setHapticFeedback(enabled) }
    }
}
