package com.zmosoft.weatherplatform.android.compose.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.zmosoft.weatherplatform.android.R
import com.zmosoft.weatherplatform.android.compose.WeatherPlatformTheme
import com.zmosoft.weatherplatform.android.utils.*
import com.zmosoft.weatherplatform.repositories.RepositoryDataContainer
import com.zmosoft.weatherplatform.repositories.data.WeatherData
import kotlinx.coroutines.launch

@Composable
fun WeatherSearchScreen(
    modifier: Modifier = Modifier,
    onLocationClicked: suspend () -> Unit
) {
    val content = LocalRepositoryContent.current
    val interfaces = content.interfaces
    val data = content.data
    val weatherData = data.weatherData.data
    val autocompleteResults = data.googleMapsData.autocompletePredictions
    val loading = (data.googleMapsData.loading || data.weatherData.loading)
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
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        interfaces?.googleMapsInterface?.placesAutoComplete(searchQuery)
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
                        interfaces?.weatherInterface?.searchWeather(searchQuery)
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

        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp)
                )
            }
            autocompleteResults.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth(),
                    content = {
                        itemsIndexed(items = autocompleteResults) { i, prediction ->
                            if (i > 0) {
                                Divider()
                            }

                            Row(
                                modifier = Modifier
                                    .clickable {
                                        interfaces?.googleMapsInterface?.autocompleteResultSelected(
                                            prediction
                                        )
                                    }
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = prediction.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1.0f))
                            }
                        }
                    }
                )
            }
            weatherData != null -> {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = weatherData.name ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )

                val context = LocalContext.current
                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = weatherData.getCurrentTempStr(context) ?: "",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = weatherData.currentWeatherCondition
                        )
                    }
                    val weatherIconUrl = weatherData.getWeatherIconUrl(context)
                    if (weatherIconUrl?.isNotEmpty() == true) {
                        Image(
                            modifier = Modifier.size(40.dp),
                            painter = rememberImagePainter(weatherIconUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.weight(1.0f))
                }

                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1.0f))
                    weatherData.getWindIcon(context)?.let { windIcon ->
                        Image(
                            painter = painterResource(id = windIcon),
                            contentDescription = null
                        )
                    }
                    Text(text = weatherData.getWindStr(context))
                    Spacer(modifier = Modifier.weight(1.0f))
                }

                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1.0f))
                    weatherData.sunriseIcon?.let { sunriseIcon ->
                        Image(
                            painter = painterResource(id = sunriseIcon),
                            contentDescription = null
                        )
                        Text(text = weatherData.sunriseStr)
                    }
                    Spacer(modifier = Modifier.weight(1.0f))
                    weatherData.sunsetIcon?.let { sunsetIcon ->
                        Image(
                            painter = painterResource(id = sunsetIcon),
                            contentDescription = null
                        )
                        Text(text = weatherData.sunsetStr)
                    }
                    Spacer(modifier = Modifier.weight(1.0f))
                }
                Spacer(modifier = Modifier.weight(1.0f))
            }
        }
    }
}

@Preview
@Composable
fun PreviewWeatherSearchScreen() {
    CompositionLocalProvider(
        LocalRepositoryContent provides RepositoryContent(
            data = RepositoryDataContainer(
                weatherData = WeatherData(
                    data = ComposeTestData.weatherData
                )
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