package com.zmosoft.weatherplatform.android.di

import com.zmosoft.weatherplatform.android.WeatherPlatformApplication
import com.zmosoft.weatherplatform.android.mvvm.viewmodels.MainActivityViewModel
import org.kodein.di.*

object AndroidModules {
    val vmModule = DI.Module("Android/VM") {
        bind<MainActivityViewModel>() with multiton { application: WeatherPlatformApplication ->
            MainActivityViewModel(
                application = application
            )
        }
    }
}