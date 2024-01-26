package com.zmosoft.weatherplatform.android.mvvm.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.zmosoft.weatherplatform.android.di.AndroidModules
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.di.SharedModules
import com.zmosoft.weatherplatform.interfaces.GoogleMapsRepositoryState
import com.zmosoft.weatherplatform.interfaces.WeatherRepositoryState
import com.zmosoft.weatherplatform.repositories.RepositoryStateContainer
import org.kodein.di.*

class MainActivityViewModel(
    activity: Activity
): ViewModel(), DIAware {
    override val di by DI.lazy {
        importAll(
            AndroidModules.vmModule,
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

    val sharedRepositories: SharedRepositories by di.instance()

    val googleMapsState = GoogleMapsRepositoryState(
        scope = viewModelScope,
        sharedRepositories = sharedRepositories
    )

    val weatherState = WeatherRepositoryState(
        scope = viewModelScope,
        sharedRepositories = sharedRepositories
    )

    val interfaces = RepositoryStateContainer(
        googleMapsState = googleMapsState,
        weatherState = weatherState
    )

    fun updateLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                weatherState.searchWeatherByLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }
        } catch (e: SecurityException) {
        }
    }
}