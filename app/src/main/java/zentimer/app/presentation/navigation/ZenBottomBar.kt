package zentimer.app.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import zentimer.app.ui.theme.ZenAccent
import zentimer.app.ui.theme.ZenMuted
import zentimer.app.ui.theme.ZenPrimary
import zentimer.app.ui.theme.ZenSurface
import zentimer.app.ui.theme.ZenText

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun ZenBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(Screen.Timer.route, "Таймер", Icons.Default.Timer),
        BottomNavItem(Screen.Path.route, "Путь", Icons.Default.BarChart),
        BottomNavItem(Screen.Settings.route, "Настройки", Icons.Default.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = ZenSurface,
        contentColor = ZenText
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label, Modifier.size(24.dp)) },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Timer.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ZenPrimary,
                    selectedTextColor = ZenPrimary,
                    unselectedIconColor = ZenMuted,
                    unselectedTextColor = ZenMuted,
                    indicatorColor = ZenSurface
                )
            )
        }
    }
}
