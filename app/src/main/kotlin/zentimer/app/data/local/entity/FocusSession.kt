package zentimer.app.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

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
