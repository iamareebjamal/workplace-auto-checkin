package org.string.employees.geofence

import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.common.api.ApiException

const val UNKNOWN_ERROR = "Unknown geofence error"
const val GEOFENCE_NOT_AVAILABLE = "Geofence service is not available now"
const val GEOFENCE_TOO_MANY_GEOFENCE = "Your app has registered too many geofences"
const val GEOFENCE_TOO_MANY_PENDING_INTENTS = "You have provided too many PendingIntents to the addGeofences() call"

object GeofenceErrorMessages {

    fun getErrorString(e: Exception): String {
        return if (e is ApiException) {
            getErrorString(e.statusCode)
        } else {
            UNKNOWN_ERROR
        }
    }

    fun getErrorString(errorCode: Int): String {
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> GEOFENCE_NOT_AVAILABLE
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> GEOFENCE_TOO_MANY_GEOFENCE
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> GEOFENCE_TOO_MANY_PENDING_INTENTS
            else -> UNKNOWN_ERROR
        }
    }

}