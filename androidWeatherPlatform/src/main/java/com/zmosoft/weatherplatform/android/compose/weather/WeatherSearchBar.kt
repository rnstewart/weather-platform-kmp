package com.zmosoft.weatherplatform.android.compose.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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

@Composable
fun WeatherSearchBar(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onLocationSearch: (String) -> Unit,
    onWeatherSearch: (String) -> Unit,
    onLocationClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var searchQuery by remember {
        mutableStateOf("")
    }

    Row(
        modifier = modifier.padding(bottom = 16.dp),
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
                    onWeatherSearch(searchQuery)
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
                    onLocationSearch(searchQuery)
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
                    onLocationClicked()
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
}

@Preview
@Composable
fun PreviewWeatherSearchBar() {
    WeatherPlatformTheme {
        WeatherSearchBar(
            loading = false,
            onLocationSearch = {},
            onLocationClicked = {},
            onWeatherSearch = {}
        )
    }
}