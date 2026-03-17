package zentimer.app.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerManager(
    private val scope: CoroutineScope,
    private val focusDurationMinutes: Int,
    private val breakDurationMinutes: Int,
    private val onTimerComplete: () -> Unit
) {
    private val _state = MutableStateFlow(
        TimerState(
            remainingSeconds = focusDurationMinutes * 60,
            totalSeconds = focusDurationMinutes * 60,
            isRunning = false,
            isBreak = false,
            currentTask = null
        )
    )
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun setFocusMode(durationMinutes: Int) {
        if (!_state.value.isRunning) {
            _state.value = _state.value.copy(
                remainingSeconds = durationMinutes * 60,
                totalSeconds = durationMinutes * 60,
                isBreak = false
            )
        }
    }

    fun setBreakMode(durationMinutes: Int) {
        if (!_state.value.isRunning) {
            _state.value = _state.value.copy(
                remainingSeconds = durationMinutes * 60,
                totalSeconds = durationMinutes * 60,
                isBreak = true
            )
        }
    }

    fun setTask(task: String?) {
        _state.value = _state.value.copy(currentTask = task)
    }

    fun start() {
        if (_state.value.isRunning) return
        _state.value = _state.value.copy(isRunning = true)
        timerJob = scope.launch {
            while (isActive && _state.value.remainingSeconds > 0) {
                delay(1000)
                val current = _state.value
                val newRemaining = (current.remainingSeconds - 1).coerceAtLeast(0)
                _state.value = current.copy(remainingSeconds = newRemaining)
                if (newRemaining == 0) {
                    onTimerComplete()
                }
            }
            _state.value = _state.value.copy(isRunning = false)
        }
    }

    fun pause() {
        timerJob?.cancel()
        timerJob = null
        _state.value = _state.value.copy(isRunning = false)
    }

    fun toggle() {
        if (_state.value.isRunning) pause() else start()
    }

    fun reset() {
        timerJob?.cancel()
        timerJob = null
        _state.value = _state.value.copy(
            remainingSeconds = _state.value.totalSeconds,
            isRunning = false
        )
    }

    fun cancelSession() {
        timerJob?.cancel()
        timerJob = null
        _state.value = _state.value.copy(
            remainingSeconds = _state.value.totalSeconds,
            isRunning = false,
            currentTask = null
        )
    }
}
