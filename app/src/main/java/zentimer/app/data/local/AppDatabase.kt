package zentimer.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
