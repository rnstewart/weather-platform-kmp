package com.zmosoft.weatherplatform.android.utils

import androidx.compose.runtime.compositionLocalOf
import com.zmosoft.weatherplatform.repositories.RepositoryDataContainer
import com.zmosoft.weatherplatform.repositories.RepositoryInterfaceContainer

data class RepositoryContent(
    val data: RepositoryDataContainer = RepositoryDataContainer(),
    val interfaces: RepositoryInterfaceContainer? = null
)

data class MainScreenNavInterface(
    val onBackClicked: () -> Unit = {}
)

val LocalRepositoryContent = compositionLocalOf { RepositoryContent() }
val LocalMainScreenNavInterface = compositionLocalOf { MainScreenNavInterface() }