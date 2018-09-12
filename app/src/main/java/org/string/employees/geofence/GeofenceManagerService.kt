package org.string.employees.geofence

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

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
        val intent = Intent(application, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getService(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val addGeofenceLiveData = MutableLiveData<Result>()

    @SuppressLint("MissingPermission")
    fun addWorkGeofence(): LiveData<Result> {
        geofencingClient.removeGeofences(listOf(GeoConstants.WORK_GEOFENCE_ID))?.run {
            addOnSuccessListener {
                Log.d("GEOFENCE", "Removed Geofence")
            }

            addOnFailureListener {
                Log.e("GEOFENCE", "Error removing geofence", it)
            }
        }
        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                Log.d("GEOFENCE", "Added Geofence")
                addGeofenceLiveData.postValue(Success())
            }
            addOnFailureListener {
                Log.e("GEOFENCE", "Error adding geofence", it)
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