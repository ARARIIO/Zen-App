package zentimer.app.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import zentimer.app.presentation.path.PathScreen
import zentimer.app.presentation.settings.SettingsScreen
import zentimer.app.presentation.timer.TimerScreen

sealed class Screen(val route: String) {
    data object Timer : Screen("timer")
    data object Path : Screen("path")
    data object Settings : Screen("settings")
}

@Composable
fun ZenNavGraph(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { ZenBottomBar(navController = navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Timer.route,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Timer.route) { TimerScreen(navController = navController) }
            composable(Screen.Path.route) { PathScreen(navController = navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController = navController) }
        }
    }
}
