package com.zmosoft.weatherplatform.state

import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class RepositoryStateBase(
    scope: CoroutineScope?
) {
    val coroutineScope = scope ?: CoroutineScope(BackgroundDispatcher + Job())

    private val _loading = MutableStateFlow(false)
    val loading:StateFlow<Boolean> = _loading

    protected suspend fun isLoading(value: Boolean) {
        _loading.emit(value)
    }

    fun cancel() {
        coroutineScope.cancel()
    }
}