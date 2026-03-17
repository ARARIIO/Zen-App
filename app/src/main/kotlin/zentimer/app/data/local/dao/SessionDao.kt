package zentimer.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import zentimer.app.data.local.entity.FocusSession

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: FocusSession): Long

    @Query("SELECT * FROM focus_sessions WHERE startTimestamp >= :startOfDay AND startTimestamp < :endOfDay ORDER BY startTimestamp DESC")
    fun getSessionsForDay(startOfDay: Long, endOfDay: Long): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE startTimestamp >= :startOfWeek ORDER BY startTimestamp DESC")
    fun getSessionsForWeek(startOfWeek: Long): Flow<List<FocusSession>>

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE startTimestamp >= :startOfDay AND startTimestamp < :endOfDay AND isBreak = 0")
    fun getFocusMinutesForDay(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query("SELECT DISTINCT taskName FROM focus_sessions WHERE taskName != '' ORDER BY startTimestamp DESC LIMIT :limit")
    fun getRecentTaskNames(limit: Int): Flow<List<String>>
}
