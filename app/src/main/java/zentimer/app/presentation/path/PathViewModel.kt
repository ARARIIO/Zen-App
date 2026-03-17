package zentimer.app.presentation.path

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import zentimer.app.data.local.entity.FocusSession
import zentimer.app.data.repository.SessionRepository

data class PathUiState(
    val sessions: List<FocusSession>,
    val focusMinutesToday: Int
)

class PathViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val uiState: StateFlow<PathUiState> = sessionRepository.getSessionsForToday()
        .map { sessions ->
            val minutes = sessions.filter { !it.isBreak }.sumOf { it.durationMinutes }
            PathUiState(sessions = sessions, focusMinutesToday = minutes)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PathUiState(emptyList(), 0)
        )
}
