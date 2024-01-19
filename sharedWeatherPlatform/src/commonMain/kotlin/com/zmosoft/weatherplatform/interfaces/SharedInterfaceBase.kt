package com.zmosoft.weatherplatform.interfaces

import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.*

open class SharedInterfaceBase(
    scope: CoroutineScope?
) {
    val coroutineScope = scope ?: CoroutineScope(BackgroundDispatcher + Job())

    fun cancel() {
        coroutineScope.cancel()
    }
}