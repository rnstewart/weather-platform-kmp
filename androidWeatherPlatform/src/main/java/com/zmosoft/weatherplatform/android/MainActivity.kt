package com.zmosoft.weatherplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.zmosoft.weatherplatform.android.compose.WeatherPlatformTheme
import com.zmosoft.weatherplatform.android.compose.main.MainScreen
import com.zmosoft.weatherplatform.android.di.AndroidModules
import com.zmosoft.weatherplatform.android.mvvm.viewmodels.MainActivityViewModel
import com.zmosoft.weatherplatform.android.utils.LocalRepositoryContent
import com.zmosoft.weatherplatform.android.utils.RepositoryContent
import com.zmosoft.weatherplatform.di.SharedModules
import com.zmosoft.weatherplatform.repositories.RepositoryDataContainer
import org.kodein.di.*

class MainActivity : ComponentActivity(), DIAware {
    override val di by DI.lazy {
        importAll(
            AndroidModules.vmModule,
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm: MainActivityViewModel by di.instance(
            arg = (application as WeatherPlatformApplication)
        )
        viewModel = vm

        super.onCreate(savedInstanceState)
        setContent {
            WeatherPlatformTheme {
                CompositionLocalProvider(
                    LocalRepositoryContent provides RepositoryContent(
                        data = RepositoryDataContainer(
                            googleMapsData = viewModel.sharedRepositories.googleMapsRepository.data.collectAsState().value,
                            weatherData = viewModel.sharedRepositories.weatherRepository.data.collectAsState().value
                        ),
                        interfaces = viewModel.interfaces
                    )
                ) {
                    MainScreen(
                        onSearchClicked = {

                        },
                        onAutocompleteResultClicked = {

                        },
                        onLocationClicked = {

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    WeatherPlatformTheme {
        GreetingView("Hello, Android!")
    }
}
