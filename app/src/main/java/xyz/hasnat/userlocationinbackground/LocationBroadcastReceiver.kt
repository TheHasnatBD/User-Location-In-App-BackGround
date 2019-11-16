package xyz.hasnat.userlocationinbackground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationResult
import xyz.hasnat.sweettoast.SweetToast
import java.lang.Exception

class LocationBroadcastReceiver : BroadcastReceiver() {

    companion object{
        const val ACTION_PROCESSING = "xyz.hasnat.userlocationinbackground.UPDATE_LOCATION"
    }


    override fun onReceive(p0: Context?, intent : Intent?) {
        if (intent != null){
            val actionString =  intent.action

            if (ACTION_PROCESSING == actionString){
                val locationResult = LocationResult.extractResult(intent)
                if (locationResult != null){
                    val location = locationResult.lastLocation

                    val lat =location.latitude
                    val lng = location.longitude
                    val accuracy = location.accuracy
                    val speed = location.speed
                    val string = "Lat= $lat, Lng= $lng, Speed = $speed"
                    Log.e("tag", "Accuracy= $accuracy, Speed = $string")

                    try {
                        // app foreground
                        MainActivity.getInstance()!!.updateLocationOnUI(string)
                    } catch (e : Exception){
                        // for app killed mode
                        SweetToast.defaultShort(p0, string)
                    }

                }
            }
        }
    }


}
