package com.example.cliniclocator.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * Check if location permissions are already given previously in-app or through the playstore
 * on older Android versions.
 */
fun isLocationPermissionGiven(context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}

/**
 * Method to check if gps is enabled on the device
 */
fun isLocationGpsEnabled(context: Context?): Boolean {
    val locationMode: Int

    // Here we don't have to check if the version is abover 19(KITKAT) since we are supporting >23 versions
    try {
        locationMode = Settings.Secure.getInt(context?.getContentResolver(), Settings.Secure.LOCATION_MODE)

    } catch (e: Settings.SettingNotFoundException) {
        Log.e("PermissionUtils", e.toString())
        return false
    }

    return locationMode != Settings.Secure.LOCATION_MODE_OFF
}