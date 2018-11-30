
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

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if("android.intent.action.BOOT_COMPLETED" == intent!!.action){
            val alarm = Context.ALARM_SERVICE
            val am = context!!.getSystemService(alarm) as AlarmManager
            val intent2 = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 12, intent2, 0)

            am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pendingIntent)


        }

        if("android.intent.action.DATE_CHANGED"==intent.action){
            if(isMyServiceRunning(context,ForeGroundKotlin::class.java)){
                context!!.stopService(Intent(context,ForeGroundKotlin::class.java))
            }
      }


        if (isMyServiceRunning(context, ForeGroundKotlin::class.java)) {
        } else {
            val intent2 = Intent(context, ForeGroundKotlin::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context!!.startForegroundService(intent2)
            }else{
                context!!.startService(intent2)

            }
        }

    }


    private fun isMyServiceRunning(context: Context?, serviceClass: Class<ForeGroundKotlin>): Boolean {
        var k =0
        val activityManager = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val service = activityManager.getRunningServices(Int.MAX_VALUE)
        if (service.isNotEmpty()) {
            service.forEach {
                if (serviceClass.name == it.service.className && it.pid != 0) {
                    k=1
                }
            }

        }

        return k==1

    }

}