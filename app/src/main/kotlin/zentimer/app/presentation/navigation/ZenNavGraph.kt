package zentimer.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import zentimer.app.presentation.path.PathScreen
import zentimer.app.presentation.settings.SettingsScreen
import zentimer.app.presentation.timer.TimerScreen

@Composable
fun ZenNavGraph() {
    val backStack = rememberNavBackStack(TimerKey)

    Scaffold(
        bottomBar = { ZenBottomBar(backStack = backStack) }
    ) { padding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(padding),
            onBack = {
                if (backStack.size > 1) backStack.removeLastOrNull()
                else { backStack.clear(); backStack.add(TimerKey) }
            },
            entryProvider = entryProvider {
                entry<TimerKey> {
                    TimerScreen(
                        onNavigateToSettings = { backStack.add(SettingsKey) }
                    )
                }
                entry<PathKey> {
                    PathScreen(onBack = { backStack.clear(); backStack.add(TimerKey) })
                }
                entry<SettingsKey> {
                    SettingsScreen(onBack = { backStack.clear(); backStack.add(TimerKey) })
                }
            }
        )
    }
}
