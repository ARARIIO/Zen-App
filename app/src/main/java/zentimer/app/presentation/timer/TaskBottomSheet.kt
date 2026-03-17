package zentimer.app.presentation.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import zentimer.app.ui.theme.Outfit
import zentimer.app.ui.theme.ZenPrimary
import zentimer.app.ui.theme.ZenSurface
import zentimer.app.ui.theme.ZenText
import zentimer.app.ui.theme.ZenMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(
    onDismiss: () -> Unit,
    onStart: (String) -> Unit,
    viewModel: TimerViewModel = koinViewModel()
) {
    var taskInput by remember { mutableStateOf("") }
    val recentTasks by viewModel.recentTaskNames.collectAsState(initial = emptyList())
    val haptic = LocalHapticFeedback.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ZenSurface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = taskInput,
                onValueChange = { taskInput = it },
                placeholder = { Text("Я сфокусирован на...", color = ZenMuted) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = ZenText,
                    unfocusedTextColor = ZenText,
                    focusedBorderColor = ZenPrimary,
                    unfocusedBorderColor = ZenMuted,
                    cursorColor = ZenPrimary,
                    focusedLabelColor = ZenPrimary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "НЕДАВНИЕ НАМЕРЕНИЯ",
                color = ZenMuted,
                fontSize = 12.sp,
                fontFamily = Outfit
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (listOf("Дизайн", "Код", "Чтение", "Аналитика") + recentTasks).distinct().take(8).forEach { tag ->
                    Surface(
                        color = ZenSurface,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 4.dp),
                        onClick = { taskInput = tag }
                    ) {
                        Text(
                            tag,
                            color = ZenText,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                    onStart(taskInput.ifBlank { "Фокус" })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = taskInput.isNotBlank(),
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                    containerColor = ZenPrimary,
                    contentColor = androidx.compose.ui.graphics.Color.Black,
                    disabledContainerColor = ZenMuted.copy(alpha = 0.3f),
                    disabledContentColor = ZenMuted
                )
            ) {
                Text("СТАРТ →", fontFamily = Outfit, fontSize = 16.sp)
            }
        }
    }
}
