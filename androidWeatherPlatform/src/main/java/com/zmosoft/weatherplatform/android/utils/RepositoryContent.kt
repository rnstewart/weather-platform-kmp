package com.zmosoft.weatherplatform.android.utils

import androidx.compose.runtime.compositionLocalOf
import com.zmosoft.weatherplatform.repositories.*
import com.zmosoft.weatherplatform.state.*

data class RepositoryContent(
    val mainScreenState: MainScreenStateMachine.State = MainScreenStateMachine.State.Empty,
    val processIntent: (MainScreenStateMachine.Intent) -> Unit = {}
)

data class MainScreenNavInterface(
    val onBackClicked: () -> Unit = {}
)

val LocalRepositoryContent = compositionLocalOf { RepositoryContent() }
val LocalMainScreenNavInterface = compositionLocalOf { MainScreenNavInterface() }