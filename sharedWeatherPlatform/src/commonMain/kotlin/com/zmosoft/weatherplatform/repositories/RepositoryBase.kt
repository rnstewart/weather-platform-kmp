package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.GoogleMapsService
import com.zmosoft.weatherplatform.api.OpenWeatherService
import com.zmosoft.weatherplatform.di.SharedModules
import org.kodein.di.*

open class RepositoryBase: DIAware {
    final override val di by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    protected val googleMapsService: GoogleMapsService by di.instance()
    protected val openWeatherService: OpenWeatherService by di.instance()
}