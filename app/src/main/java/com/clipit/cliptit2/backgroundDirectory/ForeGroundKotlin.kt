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
import android.support.constraint.Constraints.TAG
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




    }

    override fun onDestroy() {

        }

    }

    private fun initializeLocationManager() {

    }


