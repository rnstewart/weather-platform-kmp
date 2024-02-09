package com.zmosoft.weatherplatform.android.compose.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData

@Composable
fun LocationAutocompleteResults(
    modifier: Modifier = Modifier,
    autocompleteResults: List<AutocompletePlacesData.Prediction>,
    onLocationClicked: (AutocompletePlacesData.Prediction) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        content = {
            itemsIndexed(items = autocompleteResults) { i, prediction ->
                if (i > 0) {
                    Divider()
                }

                Row(
                    modifier = Modifier
                        .clickable {
                            onLocationClicked(prediction)
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