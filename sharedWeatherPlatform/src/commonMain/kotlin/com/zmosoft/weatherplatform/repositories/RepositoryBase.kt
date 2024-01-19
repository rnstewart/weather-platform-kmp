package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.*
import com.zmosoft.weatherplatform.di.SharedModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

open class RepositoryBase: DIAware {
    final override val di by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    protected val googleMapsService: GoogleMapsService by di.instance()
    protected val openWeatherService: OpenWeatherService by di.instance()

    val error: MutableStateFlow<APIResponse.APIError?> = MutableStateFlow(null)
}