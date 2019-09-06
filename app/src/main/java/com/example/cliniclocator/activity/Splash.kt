package com.example.cliniclocator.activity

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cliniclocator.R
import com.example.cliniclocator.callback.LocationRequestCallback
import com.example.cliniclocator.callback.LocationResultCallback
import com.example.cliniclocator.util.isLocationGpsEnabled
import com.example.cliniclocator.util.isLocationPermissionGiven
import com.example.cliniclocator.util.setUpLocationRequest
import com.example.cliniclocator.util.startLocationService
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import android.content.Intent



class Splash : AppCompatActivity(), OnMapReadyCallback {

    val REQUESTCODE_LOCATION_PERMISSION: Int = 1
    val REQUESTCODE_GPS_PERMISSION: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // check if gps if open so as to open location
        checkLocationPermission()
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    /**
     * Method to check if location is allowed.
     * If not, it opens a dialog to prompt the user to give permission to open location.
     * Opens location if the user permits to openn location
     */
    private fun checkLocationPermission() {
        if (!isLocationPermissionGiven(applicationContext) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Permission is not granted, we can request the permission.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUESTCODE_LOCATION_PERMISSION)
        } else {
            openLocationGps()
        }
    }

    /**
     * Method to handle permissions request response
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUESTCODE_LOCATION_PERMISSION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    openLocationGps()
                }
                return
            }
        }
    }

    /**
     * Method to check if gps is enabled, and if not enable it
     */
    private fun openLocationGps() {

        // Check if Gps is enabled
        if (!isLocationGpsEnabled(applicationContext)) {

            // Gps not enabled, ask for gps permission, result in onActivityResult
            showDialogToEnableGps()
        } else {

            // start location service
            startLocationService(this, object : LocationResultCallback {
                override fun getLocationResults() {

                    // If this method was triggerred from map fragment, then call a method to reset the map

                    val myIntent = Intent(this@Splash, MapsActivity::class.java)
                    this@Splash.startActivity(myIntent)

                }

            })
        }
    }

    /**
     * Method to build Google Play Services location dialog
     */
    private fun showDialogToEnableGps() {
        val googleApiClient: GoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API).build()
        googleApiClient.connect()

        setUpLocationRequest(googleApiClient, object: LocationRequestCallback {
            override fun requestForLocation(status: Status) {
                try {

                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(this@Splash, 1000)
                } catch (e: IntentSender.SendIntentException) {

                    // Log the error.
                }
            }
        })
    }
}
