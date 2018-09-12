package org.string.employees

import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import org.string.employees.geofence.GeofenceManagerService
import org.string.employees.geofence.Result

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private val geofencingManagerService = GeofenceManagerService(application)

    private val askPermissionLiveData = MutableLiveData<String>()
    private val toastLiveData = MutableLiveData<String>()

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
            addGeofence()
        } else {
            toastLiveData.postValue("Permission Denied")
        }
    }

    fun addGeofence(): LiveData<Result>? {
        return if (checkAndRequestPermission()) {
            geofencingManagerService.addWorkGeofence()
        } else {
            null
        }
    }

}