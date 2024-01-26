package com.zmosoft.weatherplatform.android.utils

import androidx.compose.runtime.compositionLocalOf
import com.zmosoft.weatherplatform.repositories.*

data class RepositoryContent(
    val data: RepositoryDataContainer = RepositoryDataContainer(),
    val loadingState: LoadingState = LoadingState(),
    val errorState: ErrorState = ErrorState(),
    val interfaces: RepositoryStateContainer? = null
)

data class MainScreenNavInterface(
    val onBackClicked: () -> Unit = {}
)

val LocalRepositoryContent = compositionLocalOf { RepositoryContent() }
val LocalMainScreenNavInterface = compositionLocalOf { MainScreenNavInterface() }