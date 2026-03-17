package zentimer.app.presentation.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import zentimer.app.data.local.entity.FocusSession
import zentimer.app.data.preferences.UserPreferencesRepository
import zentimer.app.data.repository.SessionRepository
import zentimer.app.domain.TimerManager
import zentimer.app.domain.TimerState

class TimerViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var timerManager: TimerManager? = null

    private val _showTaskSheet = MutableStateFlow(false)
    val showTaskSheet: StateFlow<Boolean> = _showTaskSheet.asStateFlow()

    private val _timerState = MutableStateFlow<TimerState?>(null)
    val timerState: StateFlow<TimerState?> = _timerState.asStateFlow()

    private val _isBreakMode = MutableStateFlow(false)
    val isBreakMode: StateFlow<Boolean> = _isBreakMode.asStateFlow()

    val recentTaskNames: StateFlow<List<String>> = sessionRepository.getRecentTaskNames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val prefs = preferencesRepository.preferences.first()
            timerManager = TimerManager(
                scope = viewModelScope,
                focusDurationMinutes = prefs.focusDurationMinutes,
                breakDurationMinutes = prefs.shortBreakMinutes,
                onTimerComplete = { onTimerComplete() }
            )
            timerManager?.state?.onEach { _timerState.value = it }?.launchIn(viewModelScope)
        }

        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                if (!(_timerState.value?.isRunning == true)) {
                    if (_isBreakMode.value) {
                        timerManager?.setBreakMode(prefs.shortBreakMinutes)
                    } else {
                        timerManager?.setFocusMode(prefs.focusDurationMinutes)
                    }
                }
            }
        }
    }

    private fun onTimerComplete() {
        viewModelScope.launch {
            val state = _timerState.value ?: return@launch
            val session = FocusSession(
                taskName = state.currentTask ?: "",
                category = null,
                durationMinutes = state.totalSeconds / 60,
                startTimestamp = System.currentTimeMillis() - state.totalSeconds * 1000L,
                isBreak = state.isBreak
            )
            sessionRepository.saveSession(session)
            _isBreakMode.value = !_isBreakMode.value
            val prefs = preferencesRepository.preferences.first()
            if (_isBreakMode.value) {
                timerManager?.setBreakMode(prefs.shortBreakMinutes)
            } else {
                timerManager?.setFocusMode(prefs.focusDurationMinutes)
            }
        }
    }

    fun openTaskSheet() {
        _showTaskSheet.value = true
    }

    fun closeTaskSheet() {
        _showTaskSheet.value = false
    }

    fun startSession(task: String) {
        timerManager?.setTask(task)
        timerManager?.start()
        _showTaskSheet.value = false
    }

    fun toggleTimer() {
        timerManager?.toggle()
    }

    fun cancelSession() {
        timerManager?.cancelSession()
    }

    fun switchMode(isBreak: Boolean) {
        if (_timerState.value?.isRunning == true) return
        viewModelScope.launch {
            val prefs = preferencesRepository.preferences.first()
            _isBreakMode.value = isBreak
            if (isBreak) {
                timerManager?.setBreakMode(prefs.shortBreakMinutes)
            } else {
                timerManager?.setFocusMode(prefs.focusDurationMinutes)
            }
        }
    }
}
