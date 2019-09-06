package com.example.cliniclocator.util

import android.content.Context
import android.location.Location
import android.util.Log
import com.example.cliniclocator.LocationService
import com.example.cliniclocator.PreferenceEnum
import com.example.cliniclocator.callback.LocationKnownCallback
import com.example.cliniclocator.callback.LocationResultCallback
import com.example.cliniclocator.variables.PreferenceVariables


private var mLocationService: LocationService? = null

private val TAG = "LocationUtils"

/**
 * Method to trigger location.
 * Contains callback method when location is known.
 * Start location service and register callback for receiving location
 */
fun startLocationService(context: Context, listner: LocationResultCallback) {

    if (mLocationService == null) {

        mLocationService = LocationService()
    }

    // set callback
    mLocationService?.setLocationKnownCallback(object: LocationKnownCallback {
        override fun onAction(location: Location) {

            // set longitude and latitude when location is found
            if (location.longitude != 0.0 && location.latitude != 0.0) {

                Log.e( TAG, "Location found - Latitude: " + location.latitude)
                Log.e(TAG, "Location found - Latitude: " + location.longitude)

                // save location
                PreferenceVariables.setDoublePreference(PreferenceEnum.LATITUDE, location.latitude, context)
                PreferenceVariables.setDoublePreference(PreferenceEnum.LONGITUDE, location.longitude, context)

                // check if location was saved successfully and stop animation
                if (PreferenceVariables.getDoublePreference(context, PreferenceEnum.LATITUDE) != 0.0
                    && PreferenceVariables.getDoublePreference(context, PreferenceEnum.LONGITUDE) != 0.0) {

                    listner.getLocationResults()

                    Log.e(TAG, "Location was saved successfully!")
                }
            }
        }
    } )

    // initialize location service
    mLocationService?.initLocationService(context)
}