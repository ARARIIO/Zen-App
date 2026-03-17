package zentimer.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import zentimer.app.data.audio.BackgroundSoundPlayer
import zentimer.app.data.local.AppDatabase
import zentimer.app.data.preferences.UserPreferencesRepository
import zentimer.app.data.repository.SessionRepository
import zentimer.app.domain.SoundPlayer

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zen_timer_prefs")

val appModule = module {
    single { get<Context>().dataStore }
    single<SoundPlayer> { BackgroundSoundPlayer(get()) }
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "zen_timer_db").build() }
    single { get<AppDatabase>().sessionDao() }
    single { UserPreferencesRepository(get()) }
    single { SessionRepository(get()) }
}
