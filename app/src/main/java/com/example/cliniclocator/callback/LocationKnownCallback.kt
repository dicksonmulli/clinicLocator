package com.example.cliniclocator.callback

import android.location.Location

interface LocationKnownCallback {

    /**
     * Callback when location is known
     */
    fun onAction(location: Location)
}

