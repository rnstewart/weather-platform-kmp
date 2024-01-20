package com.zmosoft.weatherplatform.utils

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

actual object PlatformInfo {
    actual fun appReference(): String {
        return "ios"
    }

    actual fun appVersion(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String) ?: ""
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun isDebug(): Boolean {
        return Platform.isDebugBinary
    }
}