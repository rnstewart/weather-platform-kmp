package com.zmosoft.weatherplatform.android.compose.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.zmosoft.weatherplatform.android.utils.*
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

@Composable
fun WeatherDataScreen(
    modifier: Modifier = Modifier,
    data: WeatherDataResponse
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = data.name ?: "",
            style = MaterialTheme.typography.headlineMedium
        )

        val context = LocalContext.current
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = data.getCurrentTempStr(context) ?: "",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = data.currentWeatherCondition
                )
            }
            val weatherIconUrl = data.getWeatherIconUrl(context)
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
            data.getWindIcon(context)?.let { windIcon ->
                Image(
                    painter = painterResource(id = windIcon),
                    contentDescription = null
                )
            }
            Text(text = data.getWindStr(context))
            Spacer(modifier = Modifier.weight(1.0f))
        }

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1.0f))
            data.sunriseIcon?.let { sunriseIcon ->
                Image(
                    painter = painterResource(id = sunriseIcon),
                    contentDescription = null
                )
                Text(text = data.sunriseStr)
            }
            Spacer(modifier = Modifier.weight(1.0f))
            data.sunsetIcon?.let { sunsetIcon ->
                Image(
                    painter = painterResource(id = sunsetIcon),
                    contentDescription = null
                )
                Text(text = data.sunsetStr)
            }
            Spacer(modifier = Modifier.weight(1.0f))
        }
        Spacer(modifier = Modifier.weight(1.0f))
    }
}