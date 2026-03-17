package zentimer.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import zentimer.app.data.preferences.UserPreferencesRepository
import zentimer.app.ui.theme.Outfit
import zentimer.app.ui.theme.ZenAccent
import zentimer.app.ui.theme.ZenBackground
import zentimer.app.ui.theme.ZenMuted
import zentimer.app.ui.theme.ZenPrimary
import zentimer.app.ui.theme.ZenSurface
import zentimer.app.ui.theme.ZenText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
    ) {
        TopAppBar(
            title = { Text("Настройки", color = ZenText) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = ZenText)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ZenBackground,
                titleContentColor = ZenText
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("ИНТЕРВАЛЫ", color = ZenMuted, fontSize = 12.sp, fontFamily = Outfit)
            Spacer(modifier = Modifier.height(16.dp))

            SettingSlider(
                label = "Фокус",
                value = preferences.focusDurationMinutes,
                valueRange = 15f..60f,
                steps = 8,
                color = ZenPrimary,
                onValueChange = { viewModel.setFocusDuration(it.toInt()) }
            )
            SettingSlider(
                label = "Короткий перерыв",
                value = preferences.shortBreakMinutes,
                valueRange = 5f..15f,
                steps = 4,
                color = ZenAccent,
                onValueChange = { viewModel.setShortBreak(it.toInt()) }
            )
            SettingSlider(
                label = "Длинный перерыв",
                value = preferences.longBreakMinutes,
                valueRange = 15f..30f,
                steps = 4,
                color = ZenAccent,
                onValueChange = { viewModel.setLongBreak(it.toInt()) }
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text("ФОНОВЫЕ ЗВУКИ", color = ZenMuted, fontSize = 12.sp, fontFamily = Outfit)
            Spacer(modifier = Modifier.height(16.dp))

            SoundOption("Без звука", UserPreferencesRepository.SOUND_NONE, preferences.backgroundSound) {
                viewModel.setBackgroundSound(it)
            }
            SoundOption("Природа", UserPreferencesRepository.SOUND_NATURE, preferences.backgroundSound) {
                viewModel.setBackgroundSound(it)
            }
            SoundOption("Космос", UserPreferencesRepository.SOUND_SPACE, preferences.backgroundSound) {
                viewModel.setBackgroundSound(it)
            }
            SoundOption("Белый шум", UserPreferencesRepository.SOUND_WHITE_NOISE, preferences.backgroundSound) {
                viewModel.setBackgroundSound(it)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("СИСТЕМА", color = ZenMuted, fontSize = 12.sp, fontFamily = Outfit)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Тактильный отклик", color = ZenText, fontFamily = Outfit)
                Switch(
                    checked = preferences.hapticFeedbackEnabled,
                    onCheckedChange = {
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        viewModel.setHapticFeedback(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ZenPrimary,
                        checkedTrackColor = ZenPrimary.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingSlider(
    label: String,
    value: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    color: androidx.compose.ui.graphics.Color,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = ZenText, fontFamily = Outfit)
            Text("$value м", color = color, fontFamily = Outfit)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it) },
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )
    }
}

@Composable
private fun SoundOption(
    label: String,
    value: String,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = ZenText, fontFamily = Outfit, modifier = Modifier.weight(1f))
        RadioButton(
            selected = selected == value,
            onClick = { onSelect(value) },
            colors = RadioButtonDefaults.colors(
                selectedColor = ZenPrimary
            )
        )
    }
}
