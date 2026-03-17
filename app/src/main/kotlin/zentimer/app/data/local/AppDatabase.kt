package zentimer.app.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import zentimer.app.data.local.dao.SessionDao
import zentimer.app.data.local.entity.FocusSession

@Database(
    entities = [FocusSession::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
