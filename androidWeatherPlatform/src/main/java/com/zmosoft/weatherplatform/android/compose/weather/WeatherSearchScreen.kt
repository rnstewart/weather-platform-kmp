package com.zmosoft.weatherplatform.android.compose.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zmosoft.weatherplatform.android.R
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
    val content = LocalRepositoryContent.current
    val state = content.mainScreenState
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1.0f),
                value = searchQuery,
                label = {
                    Text(text = stringResource(id = R.string.search_location))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Image(
                            modifier = Modifier.clickable {
                                searchQuery = ""
                            },
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                },
                onValueChange = {
                    searchQuery = it
                }
            )

            if (searchQuery.isNotEmpty()) {
                val loading = (state is MainScreenState.Loading)

                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        content.processIntent(
                            MainScreenIntent.SearchLocation(searchQuery)
                        )
                    },
                    enabled = !loading
                ) {
                    Image(
                        imageVector = Icons.Filled.List,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                }

                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        content.processIntent(
                            MainScreenIntent.SearchWeatherByName(searchQuery)
                        )
                    },
                    enabled = !loading
                ) {
                    Image(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            onLocationClicked()
                        }
                    }
                ) {
                    Image(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

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
        LocalRepositoryContent provides RepositoryContent(
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