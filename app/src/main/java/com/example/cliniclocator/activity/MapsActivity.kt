package com.example.cliniclocator.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.cliniclocator.PreferenceEnum
import com.example.cliniclocator.R
import com.example.cliniclocator.util.isLocationGpsEnabled
import com.example.cliniclocator.util.isLocationPermissionGiven
import com.example.cliniclocator.variables.PreferenceVariables

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var mLongLatCoordinates: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val latitude = PreferenceVariables.getDoublePreference(this, PreferenceEnum.LATITUDE)
        val longitude = PreferenceVariables.getDoublePreference(this, PreferenceEnum.LONGITUDE)

        if (latitude != null
            && longitude != null
            && PreferenceVariables.getDoublePreference(this, PreferenceEnum.LATITUDE) != 0.0
            && PreferenceVariables.getDoublePreference(this, PreferenceEnum.LONGITUDE) != 0.0) {

            // store pair of latitude and longitude coordinates, stored as degrees.
            mLongLatCoordinates = LatLng(latitude.toDouble(), longitude.toDouble())

        } else {
            // Add a marker in Nairobi and move the camera
            mLongLatCoordinates = LatLng(-1.2309, 36.7028)
        }

        mMap.addMarker(MarkerOptions().position(mLongLatCoordinates).title("AAR Clinic"))

        // move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLongLatCoordinates, 15f))


            // enable map ui settings
            if (isLocationPermissionGiven(this) && isLocationGpsEnabled(this)) setUpMapUiSettings(googleMap)
    }

    @SuppressLint("MissingPermission")
    private fun setUpMapUiSettings(googleMap: GoogleMap?) {

        // Adds a compass on the map
        googleMap?.uiSettings?.isCompassEnabled = true

        // add blue dot
        googleMap?.isMyLocationEnabled = true

        // add my location button
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true

//        // set up my location button as an ImageView
//        val locationButton: ImageView = (mMapFragmentView?.findViewById<View>
//            (Integer.parseInt("1"))
//            ?.parent as View)
//            .findViewById(Integer.parseInt("2"))
//
//        // set Icon
//        val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
//
//        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT
//        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT
//
//        // position on top right
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
//
//        layoutParams.setMargins(0, 30, 0, 0)
    }
}
