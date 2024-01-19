package com.zmosoft.weatherplatform.di

import com.zmosoft.weatherplatform.api.ApiConfig
import com.zmosoft.weatherplatform.api.GoogleMapsService
import com.zmosoft.weatherplatform.api.OpenWeatherService
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.repositories.GoogleMapsRepository
import com.zmosoft.weatherplatform.repositories.WeatherRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object SharedModules {
    private val sharedRepositories by lazy {
        SharedRepositories()
    }

    val dataModule = DI.Module("Shared/Data") {
        bind<SharedRepositories>() with singleton {
            sharedRepositories
        }
        bind {
            singleton {
                ApiConfig.apiKeys
            }
        }
        bind {
            singleton {
                OpenWeatherService(
                    apiKeys = instance()
                )
            }
        }
        bind {
            singleton {
                GoogleMapsService(
                    apiKeys = instance()
                )
            }
        }
    }
    val repositoriesModule = DI.Module("Shared/Repositories") {
        bind {
            singleton {
                WeatherRepository()
            }
        }
        bind {
            singleton {
                GoogleMapsRepository()
            }
        }
    }
}