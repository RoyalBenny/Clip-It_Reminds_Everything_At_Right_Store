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
package com.clipit.cliptit2.tabbedViewDirectory

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.clipit.cliptit2.backgroundDirectory.AlarmReceiver
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.globalVariablesDirectory.ItemClass
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.clipit.cliptit2.backgroundDirectory.NearShopIndicatorClass
import com.clipit.cliptit2.R
import com.clipit.cliptit2.con
import com.google.android.gms.maps.model.LatLng
import java.util.*

class Items : Fragment() {

    private var dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
    private var calender = Calendar.getInstance()
    private val date = dateFormat.format(calender.time)
    private var userLastLocation : Location? = null
    private var locationManager : LocationManager? = null



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val expand = view.findViewById<ExpandableListView>(R.id.expandable_list_view_at_bottom_sheet_bar)
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

        } else {
            try {
                userLastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        val db = JsonDataBase(con)
        val idArray = db.returnIdBasedOnDate(date)
        if (idArray.isNotEmpty()) {
            val sharedPerfeneceSettings = context!!.getSharedPreferences("Settings",android.content.Context.MODE_PRIVATE)
            val number = sharedPerfeneceSettings.getInt("Tracking",500)
            val nearShopIndicatorClass = NearShopIndicatorClass(context!!, idArray, number)
            try {
                val placeIndicatorArray = nearShopIndicatorClass.placeIndicatorForNearToShop(LatLng(userLastLocation!!.latitude, userLastLocation!!.longitude))
                val jsonShopArray = ArrayList<ShopClass>()
                placeIndicatorArray.forEach {
                    jsonShopArray.addAll(db.readJsonFromId(it.id!!))
                }

                val nearShopArray = nearShopIndicatorClass.shopIndicatorNearToShop(LatLng(userLastLocation!!.latitude, userLastLocation!!.longitude), jsonShopArray)
                val parentArray: ArrayList<ShopClass> = ArrayList()
                val childArray: ArrayList<ArrayList<ItemClass>> = ArrayList()
                nearShopArray.forEach {


                    parentArray.addAll(idArray.filter { pi ->
                        pi.id == it
                    })
                    childArray.add(db.readItemFromId(it))
                }
                nearShopMap.clear()
                parentArray.forEach {
                    nearShopMap[it.shopCategory!!] = it.id!!
                }
                expand.setAdapter(NearShopExpandListView(context!!, expand, parentArray, childArray))


            }catch (e:Exception){
                e.printStackTrace()
            }



        }
        super.onViewCreated(view, savedInstanceState)
    }






    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode==1){

            if(ContextCompat.checkSelfPermission(context!!,android.Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){


                userLastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)


                val alarm = Context.ALARM_SERVICE
                val am = context!!.getSystemService(alarm) as AlarmManager
                val intent2 = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 12, intent2, 0)

                val type = AlarmManager.RTC
                am.setRepeating(type, System.currentTimeMillis(), 60000, pendingIntent)


            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




}

