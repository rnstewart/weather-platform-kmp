package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

open class RepositoryStateBase(
    scope: CoroutineScope?
) {
    val loading = MutableStateFlow(false)
    val coroutineScope = scope ?: CoroutineScope(BackgroundDispatcher + Job())

    fun cancel() {
        coroutineScope.cancel()
    }
}