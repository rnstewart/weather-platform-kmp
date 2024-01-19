package com.zmosoft.weatherplatform.utils

import platform.Foundation.NSBundle

actual object PlatformInfo {
    actual fun appReference(): String {
        return "ios"
    }

    actual fun appVersion(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String) ?: ""
    }

    actual fun isDebug(): Boolean {
        return Platform.isDebugBinary
    }
}