package com.zmosoft.weatherplatform.android.mvvm.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zmosoft.weatherplatform.android.WeatherPlatformApplication
import com.zmosoft.weatherplatform.android.di.AndroidModules
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.di.SharedModules
import com.zmosoft.weatherplatform.interfaces.SharedGoogleMapsInterface
import com.zmosoft.weatherplatform.interfaces.SharedWeatherInterface
import com.zmosoft.weatherplatform.repositories.RepositoryInterfaceContainer
import org.kodein.di.*

class MainActivityViewModel(
    application: WeatherPlatformApplication
): AndroidViewModel(application), DIAware {
    override val di by DI.lazy {
        importAll(
            AndroidModules.vmModule,
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    val sharedRepositories: SharedRepositories by di.instance()

    private val googleMapsInterface = SharedGoogleMapsInterface(
        scope = viewModelScope,
        sharedRepositories = sharedRepositories
    )

    private val weatherInterface = SharedWeatherInterface(
        scope = viewModelScope,
        sharedRepositories = sharedRepositories
    )

    val interfaces = RepositoryInterfaceContainer(
        googleMapsInterface = googleMapsInterface,
        weatherInterface = weatherInterface
    )
}