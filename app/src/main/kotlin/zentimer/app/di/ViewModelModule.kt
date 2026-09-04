package zentimer.app.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import zentimer.app.presentation.path.PathViewModel
import zentimer.app.presentation.settings.SettingsViewModel
import zentimer.app.presentation.timer.TimerViewModel

val viewModelModule = module {
    viewModel { TimerViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { PathViewModel(get()) }
}
