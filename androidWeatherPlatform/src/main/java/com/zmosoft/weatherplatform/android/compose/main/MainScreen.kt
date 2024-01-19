package com.zmosoft.weatherplatform.android.compose.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zmosoft.weatherplatform.android.compose.WeatherPlatformTheme
import com.zmosoft.weatherplatform.android.compose.weather.WeatherSearchScreen
import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    autocompleteResults: List<AutocompletePlacesData.Prediction> = listOf(),
    loading: Boolean = false,
    onSearchClicked: (String) -> Unit,
    onAutocompleteResultClicked: (AutocompletePlacesData.Prediction) -> Unit,
    onLocationClicked: () -> Unit
) {
    Scaffold(modifier = modifier) {
        WeatherSearchScreen(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            autocompleteResults = autocompleteResults,
            loading = loading,
            onSearchClicked = onSearchClicked,
            onAutocompleteResultClicked = onAutocompleteResultClicked,
            onLocationClicked = onLocationClicked
        )
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    WeatherPlatformTheme {
        MainScreen(
            onSearchClicked = {},
            onAutocompleteResultClicked = {},
            onLocationClicked = {}
        )
    }
}