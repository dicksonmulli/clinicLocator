package com.example.cliniclocator.util

import android.util.Log
import com.example.cliniclocator.callback.LocationRequestCallback
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*

fun setUpLocationRequest(googleApiClient: GoogleApiClient, callback: LocationRequestCallback) {

    val locationRequest: LocationRequest = LocationRequest.create()


    // PRIORITY_BALANCED_POWER_ACCURACY is more likely to use WIFI & Cell tower positioning
    locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

    locationRequest.interval = 5000

    // This allows your application to passively acquire locations at a rate faster than it actively acquires locations, saving power.
    // Unlike setInterval(long), this parameter is exact. Your application will never receive updates faster than this value.
    // If you don't call this method, a fastest interval will be selected for you. It will be a value faster than your active interval
    locationRequest.fastestInterval = 5000 / 2

    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)

    val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

    result.setResultCallback(object : ResultCallback<LocationSettingsResult> {
        override fun onResult(locationSettingsResult: LocationSettingsResult) {
            val status = locationSettingsResult.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {

                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    Log.i( "GlobalUtils", ">>>>>>>>>>>> All location settings are satisfied (gps enabled).")
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i("GlobalUtils", ">>>>>>>>>>>> Location settings are not satisfied. Show the user a dialog to upgrade location settings ")

                    callback.requestForLocation(status)
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    // The dialog is to enable location but even on airplane mode, we can still get this working with LocationManager (SkatespotPage)
                    Log.i("GlobalUtils", ">>>>>>>>>>>> Location settings are inadequate, and cannot be fixed here. Dialog not created.")
                }
            }
        }

    })
}
