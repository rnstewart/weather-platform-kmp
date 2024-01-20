package com.zmosoft.weatherplatform.android.compose.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zmosoft.weatherplatform.android.compose.WeatherPlatformTheme
import com.zmosoft.weatherplatform.android.compose.weather.WeatherSearchScreen
import com.zmosoft.weatherplatform.android.utils.*
import com.zmosoft.weatherplatform.repositories.RepositoryDataContainer
import com.zmosoft.weatherplatform.repositories.data.WeatherData

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLocationClicked: () -> Unit
) {
    Scaffold(modifier = modifier) {
        WeatherSearchScreen(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            onLocationClicked = onLocationClicked
        )
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    WeatherPlatformTheme {
        CompositionLocalProvider(
            LocalRepositoryContent provides RepositoryContent(
                data = RepositoryDataContainer(
                    weatherData = WeatherData(
                        data = ComposeTestData.weatherData
                    )
                )
            )
        ) {
            MainScreen(
                onLocationClicked = {}
            )
        }
    }
}