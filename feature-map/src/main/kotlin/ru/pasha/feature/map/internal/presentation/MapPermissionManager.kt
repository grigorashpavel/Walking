package ru.pasha.feature.map.internal.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import javax.inject.Inject

internal class MapPermissionManager @Inject constructor(
    private val context: Context,
) {
    fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun registerPermissionLauncher(
        fragment: Fragment,
        onResult: (Boolean) -> Unit
    ) = fragment.registerForActivityResult(
        LocationPermission()
    ) { result ->
        onResult(result.granted)
    }
}

internal class LocationPermission : ActivityResultContract<Void?, LocationPermissionResult>() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(ActivityResultContracts.RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS).putExtra(
            ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS, permissions)
    }

    override fun getSynchronousResult(
        context: Context,
        input: Void?
    ): SynchronousResult<LocationPermissionResult>? {
        val allGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return if (allGranted) {
            SynchronousResult(LocationPermissionResult(true))
        } else {
            null
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): LocationPermissionResult {
        if (resultCode != Activity.RESULT_OK) return LocationPermissionResult(false)
        if (intent == null) return LocationPermissionResult(false)
        val grantResults = intent.getIntArrayExtra(
            ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)
        return LocationPermissionResult(grantResults?.all { it == PackageManager.PERMISSION_GRANTED } == true)
    }
}

@Suppress("UseDataClass")
internal class LocationPermissionResult(val granted: Boolean)
