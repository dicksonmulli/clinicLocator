package com.example.cliniclocator

enum class PreferenceEnum constructor(val key: String) {

    LONGITUDE("longitude"),
    LATITUDE("latitude"),
    CITY("city"),
    COUNTRY("country"),
    COUNTRY_CODE("country code"),
    CITY_LAT("city lat"),
    CITY_LONG("city long"),
    COUNTRY_LONG("country long"),
    COUNTRY_LAT("country lat")

}