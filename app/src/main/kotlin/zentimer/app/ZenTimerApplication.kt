package zentimer.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import zentimer.app.di.appModule
import zentimer.app.di.viewModelModule

class ZenTimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ZenTimerApplication)
            modules(appModule, viewModelModule)
        }
    }
}
