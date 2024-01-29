package com.zmosoft.weatherplatform.android.di

import android.app.Activity
import com.zmosoft.weatherplatform.android.mvvm.viewmodels.MainActivityViewModel
import org.kodein.di.*

object AndroidModules {
    val vmModule = DI.Module("Android/VM") {
        bind<MainActivityViewModel>() with factory { activity: Activity ->
            MainActivityViewModel(
                activity = activity,
                sharedRepositories = instance()
            )
        }
    }
}