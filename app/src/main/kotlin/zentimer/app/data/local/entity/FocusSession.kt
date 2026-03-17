package zentimer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskName: String,
    val category: String?,
    val durationMinutes: Int,
    val startTimestamp: Long,
    val isBreak: Boolean
)
