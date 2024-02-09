package com.zmosoft.weatherplatform.android.utils

import androidx.compose.runtime.compositionLocalOf
import com.zmosoft.weatherplatform.state.*

data class ScreenStateContent(
    val mainScreenState: MainScreenState = MainScreenState.Empty,
    val processIntent: (MainScreenIntent) -> Unit = {}
)

val LocalScreenStateContent = compositionLocalOf { ScreenStateContent() }