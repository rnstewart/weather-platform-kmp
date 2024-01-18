buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)

    kotlin("plugin.serialization").version("1.9.20").apply(false)
    id("app.cash.sqldelight").version("2.0.0").apply(false)
}
