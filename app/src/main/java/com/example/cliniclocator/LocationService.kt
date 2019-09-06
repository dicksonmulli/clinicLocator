package com.example.cliniclocator

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.example.cliniclocator.callback.LocationKnownCallback

class LocationService: LocationListener {

    // tag for logging
    private val TAG = javaClass.simpleName

    // The minimum distance to change updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 100

    // The minimum time between updates in milliseconds
    private val MIN_TIME_BWTN_UPDATES: Long = 1000

    private var locationManager: LocationManager? = null

    // current known mLocation
    private var mLocation: Location? = null

    // callback used for activity to be alerted when a new mLocation is known
    private var locationKnownCallback: LocationKnownCallback? = null

    /**
     * Empty Constructor
     */
    fun LocationService() {}

    /**
     * Sets up mLocation service after permissions is granted and tries to obtain a first mLocation
     */
    @Throws(SecurityException::class)
    fun initLocationService(context: Context) {
        try {
            this.locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Get GPS and network status
            locationManager?.let {

                val isGPSEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (isNetworkEnabled) {
                    it.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BWTN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)

                    if (locationManager != null) {
                        mLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        if (mLocation != null) {
                            if (this.locationKnownCallback != null) {
                                this.locationKnownCallback?.onAction(mLocation as Location)
                                Log.d(TAG, "Callback, network location is: " + mLocation?.getLatitude() + "," + mLocation?.getLongitude())
                            }
                        }
                    }
                }

                if (isGPSEnabled) {
                    it.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BWTN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)

                    if (locationManager != null) {
                        mLocation = it.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                        if (mLocation != null) {
                            if (this.locationKnownCallback != null) {
                                this.locationKnownCallback?.onAction(mLocation as Location)
                                Log.d(TAG, "Callback, gps location is: " + mLocation?.getLatitude() + "," + mLocation?.getLongitude())
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    /**
     * We want to stop location manager if provider is disabled
     */
    override fun onProviderDisabled(provider: String?) {
        stopLocationManager()
    }

    fun getLocation(): Location? {
        return mLocation
    }

    /**
     * set mLocation call back to notify us when the mLocation is found
     */
    fun setLocationKnownCallback(locationKnownCallback: LocationKnownCallback) {
        this.locationKnownCallback = locationKnownCallback
    }

    /**
     * stop mLocation service
     */
    fun stopLocationManager() {
        Log.i( TAG, "Stop Location Manager Listener!!")
        if (locationManager != null) {
            locationManager?.removeUpdates(this)
            locationManager = null
        }
    }

}
