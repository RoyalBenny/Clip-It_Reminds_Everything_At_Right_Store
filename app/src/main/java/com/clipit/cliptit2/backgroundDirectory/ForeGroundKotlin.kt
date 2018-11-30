/*
Clip-It

 Copyright (C) <2018>  <Royal Benny>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

*/
package com.clipit.cliptit2.backgroundDirectory

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.clipit.cliptit2.*
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.globalVariablesDirectory.GlobalForBackGround
import com.clipit.cliptit2.pendingDirectory.PendingActivity
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

class ForeGroundKotlin : Service() {
    private var db : JsonDataBase? = null
    private var locationManager: LocationManager? = null
    private var nearShopIndicatorClass : NearShopIndicatorClass? = null
    private var locationListeners = LocationListener(LocationManager.GPS_PROVIDER)
    private var placeDetailList : MutableList<DistanceStoreClass> = mutableListOf()
    private var shopDetailList: List<DistanceStoreClass>? =null
    val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
    var date = dateFormat.format(Calendar.getInstance().time)!!


    private val notification: Notification?
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_HIGH)

                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager!!.createNotificationChannel(channel)

                val builder = Notification.Builder(applicationContext, "channel_01").setAutoCancel(true)
                builder.build()
            } else {
                null
            }
        }



    inner class LocationListener(provider: String) : android.location.LocationListener {
        private var lastLocation: Location

        init {
            Log.e(TAG, "LocationListener Java $provider")
            lastLocation = Location(provider)
        }

        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: Java  $location")
            lastLocation.set(location)
            if(dateFormat.format(Calendar.getInstance().time) != date){
                date = dateFormat.format(Calendar.getInstance().time)
                GlobalForBackGround.arrayGlobalPlaceDetail.clear()
                GlobalForBackGround.arrayGlobalPlaceDetail.addAll(db!!.returnAutoBasedOnDate(dateFormat.format(Calendar.getInstance().time)).filter { it.delete==0 && it.brought == 0})
            }
            placeDetailList = nearShopIndicatorClass!!.placeIndicator(LatLng(location.latitude,location.longitude)).toMutableList()

            placeDetailList.forEach {
                if(db!!.returnJsonBasedOnIdAfterFilterSpecify(it.id!!).isNotEmpty()){
                    GlobalForBackGround.arrayGlobalJson.addAll(db!!.returnJsonBasedOnIdAfterFilterSpecify(it.id!!))
                    return@forEach
                }

                GlobalForBackGround.arrayGlobalJson.addAll(db!!.readJsonFromId(it.id!!))
            }


            if(placeDetailList.isNotEmpty()){


                shopDetailList = nearShopIndicatorClass!!.shopIndicator(LatLng(location.latitude,location.longitude), GlobalForBackGround.arrayGlobalJson)
                if (shopDetailList!!.isNotEmpty()){

                    val idArray:ArrayList<String> = ArrayList()
                    shopDetailList!!.forEach {
                        idArray.add(it.id!!.toString())

                    }


                    val intent = Intent(applicationContext, PendingActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    intent.putExtra("idArray",idArray)
                    val sharedPerfeneceSettings = getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
                    val pendingIntent = PendingIntent.getActivity(this@ForeGroundKotlin,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                    val builder = NotificationCompat.Builder(this@ForeGroundKotlin, CHANNEL_ID).apply {
                        setContentIntent(pendingIntent)
                        setSmallIcon(R.mipmap.ic_launcher_pointer)
                        setContentTitle("Items to Buy")
                        setContentText("Buy All!!")
                        priority = NotificationCompat.PRIORITY_DEFAULT
                        setChannelId(CHANNEL_ID)
                        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        setOnlyAlertOnce(false)
                        setAutoCancel(true)
                        setVibrate(longArrayOf(100,200,300,400,500))
                        setSound(RingtoneManager.getValidRingtoneUri(this@ForeGroundKotlin))
                        addAction(R.color.white,"Stop Notifying for ${sharedPerfeneceSettings.getInt("Disturb",5)} min", PendingIntent.getBroadcast(this@ForeGroundKotlin,0,Intent(this@ForeGroundKotlin
                                , StopAlarmReceiver::class.java),0))
                    }
                    with(NotificationManagerCompat.from(this@ForeGroundKotlin)){
                        notify(100,builder.build())
                    }


                    stopSelf()
                }

            } else
            {
                GlobalForBackGround.arrayGlobalJson.clear()
            }

        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: Java  $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: Java  $provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: Java $provider")
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        initializeLocationManager()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, "clipItApplication").setContentTitle("Clip It")
                .setContentText("Give notification when you  reach the location")
                .setContentIntent(pendingIntent).build()



        startForeground(1, notification)

        return Service.START_STICKY
    }

    override fun onCreate() {

        db = JsonDataBase(this)
        val sharedPerfeneceSettings = getSharedPreferences("Settings",android.content.Context.MODE_PRIVATE)
        val number = sharedPerfeneceSettings.getInt("Tracking",500)
        GlobalForBackGround.arrayGlobalPlaceDetail.addAll(db!!.returnAutoBasedOnDate(date).filter { it.delete ==0 && it.brought == 0})
        nearShopIndicatorClass = NearShopIndicatorClass(this, GlobalForBackGround.arrayGlobalPlaceDetail, number)

        Log.e(TAG, "onCreate")
        initializeLocationManager()
        try {
            locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                    locationListeners)
        } catch (e: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", e)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + e.message)
        }



    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        GlobalForBackGround.arrayGlobalJson.clear()
        GlobalForBackGround.arrayGlobalPlaceDetail.clear()
        super.onDestroy()
        if (locationManager != null) {
            try {
                locationManager!!.removeUpdates(locationListeners)
            } catch (e: Exception) {
                Log.i(TAG, "error at location update", e)
            }


        }

    }

    private fun initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager")
        if (locationManager == null) {
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    companion object {
        private  val TAG = "ForeGroundService"
        private  val LOCATION_INTERVAL = 10000
        private  val LOCATION_DISTANCE = 2f
    }

}