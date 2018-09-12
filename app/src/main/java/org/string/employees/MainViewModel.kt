package org.string.employees

import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import org.string.employees.geofence.GeofenceManagerService

private const val TASK_ADD_GEOFENCE = 1
private const val TASK_FETCH_LOCATION = 2

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private val geofencingManagerService = GeofenceManagerService(application)

    private val askPermissionLiveData = MutableLiveData<String>()
    private val toastLiveData = MutableLiveData<String>()

    private var pendingTasks = mutableListOf<Int>()

    fun getAskPermission(): LiveData<String> = askPermissionLiveData
    fun getToast(): LiveData<String> = toastLiveData

    fun checkAndRequestPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(getApplication(), locationPermission) != PackageManager.PERMISSION_GRANTED) {
            askPermissionLiveData.postValue(locationPermission)
            false
        } else {
            true
        }
    }

    fun handlePermissionGranted(granted: Boolean) {
        if (granted) {
            toastLiveData.postValue("Permission Granted")
            handlePendingTask()
        } else {
            toastLiveData.postValue("Permission Denied")
        }
    }

    fun getLocation() = geofencingManagerService.getLocation()

    fun getGeofence() = geofencingManagerService.getWorkGeofence()

    fun fetchLocation() {
        if (checkAndRequestPermission()) {
            geofencingManagerService.fetchLocation()
        } else {
            pendingTasks.add(TASK_FETCH_LOCATION)
        }
    }

    fun addGeofence() {
        if (checkAndRequestPermission()) {
            geofencingManagerService.addWorkGeofence()
        } else {
            pendingTasks.add(TASK_ADD_GEOFENCE)
        }
    }

    private fun handlePendingTask() {
        for (pendingTask in pendingTasks) {
            when (pendingTask) {
                TASK_ADD_GEOFENCE -> addGeofence()
                TASK_FETCH_LOCATION -> fetchLocation()
            }
        }
    }

}