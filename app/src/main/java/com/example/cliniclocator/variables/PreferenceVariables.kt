package com.example.cliniclocator.variables

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putLong
import android.util.Log
import androidx.core.content.edit
import com.example.cliniclocator.PreferenceEnum

object PreferenceVariables {

    private var mPreferences: SharedPreferences? = null
    /**
     * Save double
     */
    fun setDoublePreference(preference: PreferenceEnum, longString: Double, context: Context?) {
        if (isPreferencesObtained(context)) {
            mPreferences?.edit {putLong(preference.key, java.lang.Double.doubleToRawLongBits(longString))}
        }
    }

    fun getDoublePreference(context: Context?, preference: PreferenceEnum): Double? {
        return if (isPreferencesObtained(context)) {
            mPreferences?.getLong(preference.key, 0)?.let { java.lang.Double.longBitsToDouble(it) }
        } else {
            0.0
        }
    }

    /**
     * Obtain preferences
     */
    private fun isPreferencesObtained(context: Context?): Boolean {
        if (mPreferences != null) {
            return true
        } else {
            if (context == null) {
                return false
            }

            mPreferences = context.getSharedPreferences(context.applicationContext.packageName, Context.MODE_PRIVATE)

            if (mPreferences == null) {
                return false
            }

            return true
        }
    }

}