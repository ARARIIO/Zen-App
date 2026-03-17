package zentimer.app.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object TimerKey : NavKey

@Serializable
data object PathKey : NavKey

@Serializable
data object SettingsKey : NavKey
