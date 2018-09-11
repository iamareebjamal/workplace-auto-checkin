package org.string.employees

import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val geofencingManagerService = GeofenceManagerService(application)

    val askPermissionLiveData = MutableLiveData<String>()

    fun checkAndRequestPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(getApplication(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermissionLiveData.postValue(Manifest.permission.ACCESS_FINE_LOCATION)
            false
        } else {
            true
        }
    }

}