package zentimer.app.data.repository

import kotlinx.coroutines.flow.Flow
import zentimer.app.data.local.dao.SessionDao
import zentimer.app.data.local.entity.FocusSession
import java.util.Calendar

class SessionRepository(private val sessionDao: SessionDao) {

    fun getSessionsForToday(): Flow<List<FocusSession>> {
        val (start, end) = getTodayBoundaries()
        return sessionDao.getSessionsForDay(start, end)
    }

    fun getFocusMinutesToday(): Flow<Int?> {
        val (start, end) = getTodayBoundaries()
        return sessionDao.getFocusMinutesForDay(start, end)
    }

    fun getSessionsForWeek(): Flow<List<FocusSession>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK))
        return sessionDao.getSessionsForWeek(calendar.timeInMillis)
    }

    fun getRecentTaskNames(): Flow<List<String>> = sessionDao.getRecentTaskNames(10)

    suspend fun saveSession(session: FocusSession) = sessionDao.insert(session)

    private fun getTodayBoundaries(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val end = calendar.timeInMillis
        return start to end
    }
}
