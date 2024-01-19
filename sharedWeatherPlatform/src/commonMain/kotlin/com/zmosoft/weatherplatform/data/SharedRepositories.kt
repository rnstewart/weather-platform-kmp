package com.zmosoft.weatherplatform.data

import com.zmosoft.weatherplatform.di.SharedModules
import com.zmosoft.weatherplatform.repositories.GoogleMapsRepository
import com.zmosoft.weatherplatform.repositories.WeatherRepository
import org.kodein.di.*

class SharedRepositories: DIAware {
    override val di: DI by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    val googleMapsRepository: GoogleMapsRepository by di.instance()
    val weatherRepository: WeatherRepository by di.instance()
}