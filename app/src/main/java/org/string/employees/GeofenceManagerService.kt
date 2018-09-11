package org.string.employees

import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest

const val FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"

class GeofenceManagerService(private val application: Application) {

    private val geofencingClient by lazy {
        LocationServices.getGeofencingClient(application)
    }

    private val workGeofence by lazy {
        Geofence.Builder()
                .setRequestId(GeoConstants.WORK_GEOFENCE_ID)
                .setCircularRegion(
                        GeoConstants.LATITUDE,
                        GeoConstants.LONGITUDE,
                        GeoConstants.RADIUS_IN_METRES)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER
                        or
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(application, GeofenceTransitionsIntentService::class.java)
        PendingIntent.getService(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val addGeofenceLiveData = MutableLiveData<String>()

    fun addWorkGeofence(): LiveData<String> {
        return addGeofenceLiveData
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(workGeofence))
        }.build()
    }

}