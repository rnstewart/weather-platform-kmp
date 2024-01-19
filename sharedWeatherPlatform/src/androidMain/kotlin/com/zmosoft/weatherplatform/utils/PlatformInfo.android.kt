package com.zmosoft.weatherplatform.utils

import com.zmosoft.weatherplatform.BuildConfig
import com.zmosoft.weatherplatform.storage.AndroidStorageProvider

actual object PlatformInfo {
    actual fun appReference(): String {
        return "android"
    }

    actual fun appVersion(): String {
        return AndroidStorageProvider.appContext?.run {
            packageManager?.getPackageInfo(packageName, 0)?.versionName ?: ""
        } ?: ""
    }

    actual fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}