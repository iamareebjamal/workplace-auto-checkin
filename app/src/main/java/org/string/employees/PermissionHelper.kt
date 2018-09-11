package org.string.employees

import android.app.Activity
import android.app.AlertDialog
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity

class PermissionHelper(private val activity: Activity?) {

    private val permissionRequestCode = 23241

    fun askPermission(permission: String?) {
        if (permission != null && activity != null) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showRationaleDialog()
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(permission),
                        permissionRequestCode)
            }
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(activity)
                .setTitle("Location Permission")
                .setMessage("We need location permission from your device in order to trigger automatic " +
                        "check-in and check-out. If you do not grant the permission, this functionality " +
                        "will not work")
                .show()
    }

    fun handlePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        
    }

}