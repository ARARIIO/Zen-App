package zentimer.app.presentation.path

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import zentimer.app.data.local.entity.FocusSession
import zentimer.app.ui.theme.Outfit
import zentimer.app.ui.theme.SpaceGrotesk
import zentimer.app.ui.theme.ZenAccent
import zentimer.app.ui.theme.ZenBackground
import zentimer.app.ui.theme.ZenMuted
import zentimer.app.ui.theme.ZenPrimary
import zentimer.app.ui.theme.ZenSurface
import zentimer.app.ui.theme.ZenText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PathScreen(
    onBack: () -> Unit = {},
    viewModel: PathViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZenBackground)
    ) {
        TopAppBar(
            title = { Text("Путь", color = ZenText) },
            navigationIcon = {
                IconButton(onClick = onBack) {
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
                .padding(24.dp)
        ) {
            Text("ИТОГ ЗА ДЕНЬ", color = ZenMuted, fontSize = 12.sp, fontFamily = Outfit)
            val hours = uiState.focusMinutesToday / 60
            val mins = uiState.focusMinutesToday % 60
            Text(
                text = if (hours > 0) "${hours}ч ${mins}м" else "${mins}м",
                color = ZenPrimary,
                fontFamily = SpaceGrotesk,
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Созвездие фокуса", color = ZenText, fontFamily = Outfit)
                Text("Сегодня", color = ZenMuted, fontFamily = Outfit)
            }
            Spacer(modifier = Modifier.height(12.dp))

            FocusConstellation(sessions = uiState.sessions)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ZenPrimary)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("Фокус (25м)", color = ZenPrimary, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ZenAccent)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("Перерыв (5м)", color = ZenAccent, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("История сессий", color = ZenText, fontFamily = Outfit)
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.sessions.isEmpty()) {
                Text(
                    "Твой путь начинается здесь",
                    color = ZenMuted,
                    fontFamily = Outfit,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.sessions) { session ->
                        SessionHistoryItem(session = session)
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusConstellation(sessions: List<FocusSession>) {
    val blocks = sessions.take(16).reversed()
    val paddedBlocks: List<FocusSession?> = blocks + List((16 - blocks.size).coerceAtLeast(0)) { null }
    LazyVerticalGrid(
        columns = GridCells.Fixed(8),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(ZenSurface, androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(paddedBlocks.size) { index ->
            val session = paddedBlocks.getOrNull(index)
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            session == null -> ZenMuted.copy(alpha = 0.3f)
                            session.isBreak -> ZenAccent
                            else -> ZenPrimary
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {}
        }
    }
}

@Composable
private fun SessionHistoryItem(session: FocusSession) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            timeFormat.format(Date(session.startTimestamp)),
            color = ZenMuted,
            fontSize = 14.sp,
            fontFamily = Outfit
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                session.taskName.ifBlank { "Фокус" },
                color = ZenText,
                fontSize = 20.sp,
                fontFamily = Outfit
            )
            Text(
                if (session.isBreak) "Перерыв" else "Глубокая работа",
                color = ZenMuted,
                fontSize = 14.sp,
                fontFamily = Outfit
            )
        }
        Text(
            "${session.durationMinutes}м",
            color = ZenPrimary,
            fontSize = 16.sp,
            fontFamily = Outfit
        )
    }
}
