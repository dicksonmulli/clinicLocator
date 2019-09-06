package com.example.cliniclocator.callback

import com.google.android.gms.common.api.Status

interface LocationRequestCallback {
    fun requestForLocation(status: Status)
}