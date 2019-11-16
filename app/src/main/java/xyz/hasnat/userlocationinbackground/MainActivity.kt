package xyz.hasnat.userlocationinbackground

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import xyz.hasnat.sweettoast.SweetToast
import android.app.job.JobScheduler
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    companion object{
        internal var instance: MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }
    lateinit var locationRequest : LocationRequest
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instance = this

        /*val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }*/

        // Location permission
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // get user location updates
                    Log.e("tag", "onPermissionGranted")

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                        // Do something for Oreo and above versions
                        // with Service

                        Log.e("tag", "Oreo and above versions")
                        textView2.text = "Location for Oreo and above versions"

                    } else {
                        // do something for phones running an SDK before Oreo
                        // with broadcast receiver
                        Log.e("tag", "before Oreo -> old")
                        textView2.text = "Location for bellow Oreo"


                        updateUserLocation()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    SweetToast.warning(this@MainActivity, "Permission Rationale Should Be Shown")
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Log.e("tag", "onPermissionDenied")
                    SweetToast.info(this@MainActivity, "Location permission is must.")
                }

            }).check()

    } // ending onCreate

    private fun updateUserLocation() {
        buildLocationBuild()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, pendingIntent())
    }

    private fun pendingIntent(): PendingIntent? {
        val intent = Intent(this, LocationBroadcastReceiver::class.java)
        intent.action = LocationBroadcastReceiver.ACTION_PROCESSING
        return PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildLocationBuild() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 2000
        locationRequest.smallestDisplacement = 10f
    }

    fun updateLocationOnUI(location : String){
       this.runOnUiThread {
           Log.e("tag", "Location : $location")
           textView.text = location
        }
    }


    /*private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please turn on your GPS.")
            .setCancelable(false)
            .setPositiveButton("Go Settings") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11)
            }
            .setNegativeButton("No, Thanks") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }*/


}
