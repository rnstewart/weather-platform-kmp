package com.zmosoft.weatherplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import com.zmosoft.weatherplatform.android.compose.WeatherPlatformTheme
import com.zmosoft.weatherplatform.android.compose.main.MainScreen
import com.zmosoft.weatherplatform.android.di.AndroidModules
import com.zmosoft.weatherplatform.android.mvvm.viewmodels.MainActivityViewModel
import com.zmosoft.weatherplatform.android.utils.*
import com.zmosoft.weatherplatform.di.SharedModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.*

class MainActivity : ComponentActivity(), DIAware {
    override val di by DI.lazy {
        importAll(
            AndroidModules.vmModule,
            SharedModules.dataModule,
            SharedModules.repositoriesModule
        )
    }

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var permissionsRequest: PermissionsRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        permissionsRequest = PermissionsRequest(
            activity = this,
            permissionTypes = PermissionsRequest.PermissionTypes.LOCATION
        )

        val vm: MainActivityViewModel by di.instance(
            arg = this
        )
        viewModel = vm

        super.onCreate(savedInstanceState)

        setContent {
            WeatherPlatformTheme {
                CompositionLocalProvider(
                    LocalRepositoryContent provides RepositoryContent(
                        mainScreenState = viewModel.mainScreenState.state.collectAsState().value,
                        processIntent = { intent ->
                            viewModel.mainScreenState.process(intent)
                        }
                    )
                ) {
                    MainScreen(
                        onLocationClicked = {
                            if (permissionsRequest.checkPermissions(this)) {
                                withContext(Dispatchers.Main) {
                                    viewModel.updateLocation()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
