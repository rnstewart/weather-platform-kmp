plugins {
    id("co.touchlab.skie") version "0.6.1"
    id("app.cash.sqldelight")
    kotlin("plugin.serialization")

    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sharedWeatherPlatform"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.content.negotiation)

            implementation(libs.sqldelight.runtime)

            implementation(libs.napier)

            implementation(libs.klock)

            implementation(libs.ktor.serialization.kotlinx.json)

            api(libs.kodein.di)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth.jvm)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.logging.jvm)
            implementation(libs.ktor.client.json.jvm)
            implementation(libs.ktor.client.serialization.jvm)

            implementation(libs.sqldelight.android.driver)

            implementation(libs.slf4j.api)
            implementation(libs.slf4j.android)

            implementation(libs.klock)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.ios)

            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.zmosoft.weatherplatform"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }

    sqldelight {
        databases {
            create("WeatherPlatformDB") {
                packageName.set("com.zmosoft.weatherplatform")
            }
        }
    }
}
