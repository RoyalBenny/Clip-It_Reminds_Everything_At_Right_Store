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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val sharedPerfeneceSettings = context!!.getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        val alarm =context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(PendingIntent.getBroadcast(context,12,Intent(context, AlarmReceiver::class.java),0))
        alarm.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+sharedPerfeneceSettings.getInt("Disturb",5)*60000,60000,PendingIntent.getBroadcast(context,12,Intent(context, AlarmReceiver::class.java),0))
    }

}