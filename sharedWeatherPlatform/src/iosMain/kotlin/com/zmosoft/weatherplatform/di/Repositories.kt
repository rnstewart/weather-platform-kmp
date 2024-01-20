package com.zmosoft.weatherplatform.di

import com.zmosoft.weatherplatform.data.SharedRepositories
import org.kodein.di.*

@Suppress("unused")
class Repositories: DIAware {
    override val di by DI.lazy {
        importAll(
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    val sharedRepositories: SharedRepositories by di.instance()
}