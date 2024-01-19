package com.zmosoft.weatherplatform.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val MainDispatcher: CoroutineDispatcher = Dispatchers.Main
actual val BackgroundDispatcher: CoroutineDispatcher = Dispatchers.Default
actual val NetworkDispatcher: CoroutineDispatcher = Dispatchers.IO