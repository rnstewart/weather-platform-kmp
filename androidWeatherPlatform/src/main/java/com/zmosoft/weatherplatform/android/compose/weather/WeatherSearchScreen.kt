package com.zmosoft.weatherplatform.android.compose.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zmosoft.weatherplatform.android.compose.WeatherPlatformTheme
import com.zmosoft.weatherplatform.android.utils.*
import com.zmosoft.weatherplatform.state.MainScreenIntent
import com.zmosoft.weatherplatform.state.MainScreenState
import kotlinx.coroutines.launch

@Composable
fun WeatherSearchScreen(
    modifier: Modifier = Modifier,
    onLocationClicked: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val content = LocalScreenStateContent.current
    val state = content.mainScreenState

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherSearchBar(
            loading = (state is MainScreenState.Loading),
            onLocationSearch = { query ->
                content.processIntent(
                    MainScreenIntent.SearchWeatherByName(query)
                )
            },
            onWeatherSearch = { query ->
                content.processIntent(
                    MainScreenIntent.SearchLocation(query)
                )
            },
            onLocationClicked = {
                coroutineScope.launch {
                    onLocationClicked()
                }
            }
        )

        when (state) {
            is MainScreenState.AutocompleteLoaded -> {
                LocationAutocompleteResultsScreen(
                    modifier = Modifier.weight(1.0f),
                    autocompleteResults = state.places,
                    onLocationClicked = {
                        content.processIntent(
                            MainScreenIntent.SelectLocation(it)
                        )
                    }
                )
            }
            is MainScreenState.Error -> {
                ErrorScreen(error = state.error)
            }
            is MainScreenState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp)
                )
            }
            is MainScreenState.WeatherLoaded -> {
                WeatherDataScreen(data = state.data)
            }
            is MainScreenState.Empty -> {

            }
        }
    }
}

@Preview
@Composable
fun PreviewWeatherSearchScreen() {
    CompositionLocalProvider(
        LocalScreenStateContent provides ScreenStateContent(
            mainScreenState = MainScreenState.WeatherLoaded(
                data = ComposeTestData.weatherData
            )
        )
    ) {
        WeatherPlatformTheme(
            darkTheme = true
        ) {
            WeatherSearchScreen(
                onLocationClicked = {}
            )
        }
    }
}