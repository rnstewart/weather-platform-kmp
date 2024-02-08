package com.zmosoft.weatherplatform.android.utils

import androidx.compose.runtime.compositionLocalOf
import com.zmosoft.weatherplatform.state.*

data class RepositoryContent(
    val mainScreenState: MainScreenState = MainScreenState.Empty,
    val processIntent: (MainScreenIntent) -> Unit = {}
)

data class MainScreenNavInterface(
    val onBackClicked: () -> Unit = {}
)

val LocalRepositoryContent = compositionLocalOf { RepositoryContent() }
val LocalMainScreenNavInterface = compositionLocalOf { MainScreenNavInterface() }