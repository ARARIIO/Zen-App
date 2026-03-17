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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import zentimer.app.ui.theme.ZenMuted
import zentimer.app.ui.theme.ZenPrimary
import zentimer.app.ui.theme.ZenSurface
import zentimer.app.ui.theme.ZenText

data class BottomNavItem(
    val key: NavKey,
    val label: String,
    val icon: ImageVector
)

@Composable
fun ZenBottomBar(backStack: NavBackStack<NavKey>) {
    val currentKey = backStack.lastOrNull()
    val items = listOf(
        BottomNavItem(TimerKey, "Таймер", Icons.Default.Timer),
        BottomNavItem(PathKey, "Путь", Icons.Default.BarChart),
        BottomNavItem(SettingsKey, "Настройки", Icons.Default.Settings)
    )

    NavigationBar(
        containerColor = ZenSurface,
        contentColor = ZenText
    ) {
        items.forEach { item ->
            val selected = currentKey?.javaClass == item.key.javaClass
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label, Modifier.size(24.dp)) },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        backStack.clear()
                        backStack.add(item.key)
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
