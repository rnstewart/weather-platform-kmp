package com.zmosoft.weatherplatform.android.utils

import androidx.compose.runtime.compositionLocalOf
import com.zmosoft.weatherplatform.repositories.RepositoryDataContainer
import com.zmosoft.weatherplatform.repositories.RepositoryStateContainer

data class RepositoryContent(
    val data: RepositoryDataContainer = RepositoryDataContainer(),
    val interfaces: RepositoryStateContainer? = null
)

data class MainScreenNavInterface(
    val onBackClicked: () -> Unit = {}
)

val LocalRepositoryContent = compositionLocalOf { RepositoryContent() }
val LocalMainScreenNavInterface = compositionLocalOf { MainScreenNavInterface() }