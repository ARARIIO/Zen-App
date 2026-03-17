package zentimer.app.presentation.timer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import zentimer.app.ui.theme.GlowCyan
import zentimer.app.ui.theme.GlowViolet
import zentimer.app.ui.theme.SpaceGrotesk
import zentimer.app.ui.theme.ZenAccent
import zentimer.app.ui.theme.ZenBackground
import zentimer.app.ui.theme.ZenMuted
import zentimer.app.ui.theme.ZenPrimary
import zentimer.app.ui.theme.ZenSurface
import zentimer.app.ui.theme.ZenText

@Composable
fun TimerScreen(
    onNavigateToSettings: () -> Unit = {},
    viewModel: TimerViewModel = koinViewModel()
) {
    val timerState by viewModel.timerState.collectAsState()
    val isBreakMode by viewModel.isBreakMode.collectAsState()
    val showTaskSheet by viewModel.showTaskSheet.collectAsState()
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current

    DisposableEffect(timerState?.isRunning == true) {
        val window = (view.context as? android.app.Activity)?.window
        if (timerState?.isRunning == true) {
            window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(48.dp))
                ModeSwitch(
                    isBreak = isBreakMode,
                    onSwitch = { viewModel.switchMode(it) }
                )
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Настройки",
                        tint = ZenMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TimerRing(
                progress = timerState?.progress ?: 0f,
                isPaused = timerState?.isRunning == false && (timerState?.remainingSeconds ?: 0) < (timerState?.totalSeconds ?: 1),
                isBreak = isBreakMode,
                timeText = timerState?.formattedTime() ?: "25:00",
                onTap = {
                    haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                    viewModel.toggleTimer()
                },
                onLongPress = {
                    viewModel.cancelSession()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = timerState?.currentTask ?: "На чем мы фокусируемся?",
                color = if (timerState?.currentTask != null) ZenText else ZenMuted,
                fontSize = 16.sp,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { viewModel.openTaskSheet() }
                    )
                }
            )
        }

        if (showTaskSheet) {
            TaskBottomSheet(
                onDismiss = { viewModel.closeTaskSheet() },
                onStart = { viewModel.startSession(it) }
            )
        }
    }
}

@Composable
private fun ModeSwitch(
    isBreak: Boolean,
    onSwitch: (Boolean) -> Unit
) {
    Surface(
        color = ZenSurface,
        shape = RoundedCornerShape(18.dp)
    ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .padding(4.dp)
            ) {
                Surface(
                    color = if (!isBreak) ZenPrimary.copy(alpha = 0.3f) else ZenSurface,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(2.dp),
                    onClick = { onSwitch(false) }
                ) {
                    Text(
                        "Фокус",
                        color = if (!isBreak) ZenPrimary else ZenMuted,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 14.sp
                    )
                }
                Surface(
                    color = if (isBreak) ZenAccent.copy(alpha = 0.3f) else ZenSurface,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(2.dp),
                    onClick = { onSwitch(true) }
                ) {
                    Text(
                        "Перерыв",
                        color = if (isBreak) ZenAccent else ZenMuted,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 14.sp
                    )
                }
            }
    }
}

@Composable
private fun TimerRing(
    progress: Float,
    isPaused: Boolean,
    isBreak: Boolean,
    timeText: String,
    onTap: () -> Unit,
    onLongPress: () -> Unit
) {
    val color = if (isBreak) ZenAccent else ZenPrimary
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() },
                    onLongPress = { onLongPress() }
                )
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(320.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 2.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                drawCircle(
                    color = ZenSurface,
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeWidth)
                )

                drawArc(
                    color = color.copy(alpha = if (isPaused) pulseAlpha * 0.5f else 1f),
                    startAngle = 270f - (progress * 360f),
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    topLeft = center - Offset(radius, radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Text(
                text = timeText,
                color = color.copy(alpha = if (isPaused) pulseAlpha * 0.7f else 1f),
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Light,
                fontSize = 96.sp
            )
        }
    }
}
