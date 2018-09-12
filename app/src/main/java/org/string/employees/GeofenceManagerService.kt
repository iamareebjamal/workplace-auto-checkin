package org.string.employees

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.annotation.RequiresPermission
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest

class GeofenceManagerService(private val application: Application) {

    private val geofencingClient by lazy {
        LocationServices.getGeofencingClient(application)
    }

    private val workGeofence by lazy {
        Geofence.Builder()
                .setRequestId(GeoConstants.WORK_GEOFENCE_ID)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
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

    private val addGeofenceLiveData = MutableLiveData<Result>()

    @SuppressLint("MissingPermission")
    fun addWorkGeofence(): LiveData<Result> {
        geofencingClient.removeGeofences(GeoConstants.WORK_GEOFENCE_ID)
        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                addGeofenceLiveData.postValue(Success())
            }
            addOnFailureListener {
                addGeofenceLiveData.postValue(Error(it))
            }
        }

        return addGeofenceLiveData
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(workGeofence))
        }.build()
    }

}