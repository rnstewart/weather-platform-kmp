package com.zmosoft.weatherplatform.android.mvvm.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.zmosoft.weatherplatform.android.di.AndroidModules
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.di.SharedModules
import com.zmosoft.weatherplatform.interfaces.SharedGoogleMapsInterface
import com.zmosoft.weatherplatform.interfaces.SharedWeatherInterface
import com.zmosoft.weatherplatform.repositories.RepositoryInterfaceContainer
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

    fun updateLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                weatherInterface.searchWeather(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }
        } catch (e: SecurityException) {
        }
    }
}