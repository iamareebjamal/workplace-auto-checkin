package org.string.employees

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.string.employees.geofence.Error
import org.string.employees.geofence.Success

class MainActivityFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var tedPermission: TedPermission.Builder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getAskPermission().observe(this, Observer {
            askPermission(it)
        })

        viewModel.getToast().observe(this, Observer {
            it?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        })

        viewModel.addGeofence()?.observe(this, Observer {
            it?.let {
                when (it) {
                    is Success -> Toast.makeText(context, "Geofence added", Toast.LENGTH_SHORT).show()
                    is Error -> {
                        Log.e("GEOFENCE", "Geofence adding failed", it.exception)
                        Toast.makeText(context, "Geofence adding failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        return view
    }

    private fun askPermission(permission: String?) {
        if (tedPermission == null) {
            tedPermission = TedPermission.with(context)
                    .setPermissions(permission)
                    .setPermissionListener(object: PermissionListener {
                        override fun onPermissionGranted() {
                            viewModel.handlePermissionGranted(true)
                        }

                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                            viewModel.handlePermissionGranted(false)
                        }

                    })
                    .setRationaleTitle("Location Permission")
                    .setRationaleMessage("We need location permission from your device in order to trigger automatic " +
                            "check-in and check-out. If you do not grant the permission, this functionality " +
                            "will not work")
                    .setRationaleConfirmText("OK")
        }

        tedPermission?.check()
    }

}
