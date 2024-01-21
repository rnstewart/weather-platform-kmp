package com.zmosoft.weatherplatform.android.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionsRequest(
    activity: ComponentActivity,
    private val permissionTypes: PermissionTypes
) {
    enum class PermissionTypes {
        BLUETOOTH, NOTIFICATIONS, LOCATION
    }

    private var result: (Boolean) -> Unit = {}
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private val notificationPermissions: List<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf()
        }

    private val bluetoothPermissions: List<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }

    private val locationPermissions: List<String>
        get() = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    init {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted: Map<String, Boolean> ->
            if (permissionTypes == PermissionTypes.BLUETOOTH) {
                checkBluetoothEnabled(activity)
            }
            result(!isGranted.containsValue(false))
        }
    }

    fun permissionsGranted(activity: ComponentActivity): Boolean {
        return deniedPermissions(
            activity = activity
        ).isEmpty()
    }

    @SuppressLint("MissingPermission")
    private fun checkBluetoothEnabled(activity: ComponentActivity) {
        try {
            if (!(activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter.isEnabled) {
                activity.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun deniedPermissions(activity: ComponentActivity): List<String> {
        return when (permissionTypes) {
            PermissionTypes.BLUETOOTH -> bluetoothPermissions
            PermissionTypes.NOTIFICATIONS -> notificationPermissions
            PermissionTypes.LOCATION -> locationPermissions
        }.filter { permission ->
            if (permission.isNotEmpty()) {
                ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
        }
    }

    suspend fun checkPermissions(activity: ComponentActivity): Boolean {
        return suspendCoroutine { cont ->
            val deniedPermissions = deniedPermissions(
                activity = activity
            )

            if (deniedPermissions.isEmpty()) {
                if (permissionTypes == PermissionTypes.BLUETOOTH) {
                    checkBluetoothEnabled(activity)
                }
                cont.resume(true)
            } else {
                result = {
                    cont.resume(it)
                }
                requestPermissionLauncher.launch(deniedPermissions.toTypedArray())
            }
        }
    }
}