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
package com.clipit.cliptit2.homeDirectory

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.clipit.cliptit2.backgroundDirectory.AlarmReceiver
import com.clipit.cliptit2.R
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.clipit.cliptit2.con
import com.clipit.cliptit2.databaseDirectory.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


class BottomSheetaDialogFragment : Fragment(), OnMapReadyCallback{
    private val typefaceItem = Typeface.createFromAsset(con.assets,"fonts/NotoSerif-Regular.ttf")
    lateinit var mMap: GoogleMap
    private var locationListener : LocationListener? = null
    private var locationManager :LocationManager? = null
    private var addressDetailArray = arrayListOf<ShopClass>()
    private lateinit var mapFragment:MapView
    private lateinit var viewMap: View
    private lateinit var userLocation :LatLng
    private var idForMap:Int? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         viewMap = inflater.inflate(R.layout.map_view_in_category_bottom_sheet,container,false)
        val place = viewMap.findViewById<TextView>(R.id.place_text_in_map_view)
        val textplace = arguments!!.getString("place")
        idForMap = arguments!!.getInt("id")
        place.typeface = typefaceItem
        place.text = textplace!!



        return viewMap
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = view.findViewById(R.id.mapView)

        if (mapFragment!=null ){
            mapFragment.onCreate(null)
            mapFragment.onResume()
            mapFragment.getMapAsync(this)
        }
        val db = JsonDataBase(context!!)

        addressDetailArray  = db.returnJsonBasedOnId(idForMap!!)



    }



    fun addShopMarker() {

        addressDetailArray.forEach {

            if(it.specfied==0)
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude!!.toDouble(), it.longitude!!.toDouble())).title(it.shopName))
            else
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude!!.toDouble(), it.longitude!!.toDouble())).title(it.shopName)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        }
        if (addressDetailArray.isEmpty()) return

    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.clear()

        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        addShopMarker()
        Toast.makeText(context,"Double click the marker to specify it",Toast.LENGTH_SHORT).show()

        locationListener = object : LocationListener{
            override fun onLocationChanged(p0: Location?) {
                if(p0!=null) {
                    mMap.clear()
                    userLocation = LatLng(p0.latitude, p0.longitude)
                    mMap.addMarker(MarkerOptions().title("Your Location").position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                    addShopMarker()
                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onProviderDisabled(p0: String?) {

            }



        }

        var seleted = ""

     mMap.setOnMarkerClickListener {

        try {

            if (it.title != seleted){
                seleted = it.title
                }

            else{
                val shop = addressDetailArray.filter { ki ->
                    it.title == ki.shopName
                }

                if(shop[0].specfied == 0) {
                    val db = JsonDataBase(context!!)
                    val dataBase = db.writeDataBase()
                    val cv = ContentValues()
                    cv.put(jsonSpecifiedColumn, 1)
                    dataBase!!.update(jsonTableName, cv, "$jsonIncrementNumber=? and $jsonShopNameCloumn=?", arrayOf(shop[0].id.toString(), shop[0].shopName))
                    dataBase.close()
                    db.close()
                    addressDetailArray.clear()
                    addressDetailArray.addAll(db.returnJsonBasedOnId(idForMap!!))
                    mMap.clear()
                    addShopMarker()
                    try {
                        mMap.addMarker(MarkerOptions().title("Your Location").position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }else
                {
                    val db = JsonDataBase(context!!)
                    val dataBase = db.writeDataBase()
                    val cv = ContentValues()
                    cv.put(jsonSpecifiedColumn, 0)
                    dataBase!!.update(jsonTableName, cv, "$jsonIncrementNumber=? and $jsonShopNameCloumn=?", arrayOf(shop[0].id.toString(), shop[0].shopName))
                    dataBase.close()
                    db.close()
                    addressDetailArray.clear()
                    addressDetailArray.addAll(db.returnJsonBasedOnId(idForMap!!))
                    mMap.clear()
                    addShopMarker()
                    try {
                        mMap.addMarker(MarkerOptions().title("Your Location").position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

                    }catch (e:Exception){
                        e.printStackTrace()
                    }                }

            }


        }catch (
                e:Exception
        ){e.printStackTrace()}

            return@setOnMarkerClickListener false

        }

        mMap.setOnInfoWindowClickListener {


            try {
                if (it.title != seleted){
                    seleted = it.title
                    }

                else{
                    val shop = addressDetailArray.filter { ki ->
                        it.title == ki.shopName
                    }

                    if(shop[0].specfied == 0) {
                        val db = JsonDataBase(context!!)
                        val dataBase = db.writeDataBase()
                        val cv = ContentValues()
                        cv.put(jsonSpecifiedColumn, 1)
                        dataBase!!.update(jsonTableName, cv, "$jsonIncrementNumber=? and $jsonShopNameCloumn=?", arrayOf(shop[0].id.toString(), shop[0].shopName))
                        dataBase.close()
                        db.close()
                        addressDetailArray.clear()
                        addressDetailArray.addAll(db.returnJsonBasedOnId(idForMap!!))
                        mMap.clear()
                        addShopMarker()
                        try {
                            mMap.addMarker(MarkerOptions().title("Your Location").position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }else
                    {
                        val db = JsonDataBase(context!!)
                        val dataBase = db.writeDataBase()
                        val cv = ContentValues()
                        cv.put(jsonSpecifiedColumn, 0)
                        dataBase!!.update(jsonTableName, cv, "$jsonIncrementNumber=? and $jsonShopNameCloumn=?", arrayOf(shop[0].id.toString(), shop[0].shopName))
                        dataBase.close()
                        db.close()
                        addressDetailArray.clear()
                        addressDetailArray.addAll(db.returnJsonBasedOnId(idForMap!!))
                        mMap.clear()
                        addShopMarker()
                        try {
                            mMap.addMarker(MarkerOptions().title("Your Location").position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }

                }


            }catch (e:Exception){
                e.printStackTrace()
            }

        }



        if(ContextCompat.checkSelfPermission(context!!,android.Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)

        }
        else{
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
            val userLastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            try{
                val latLng = LatLng(userLastLocation.latitude,userLastLocation.longitude)
                mMap.addCircle(CircleOptions().center(latLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,3f))
                addShopMarker()
            }catch (e:Exception){
                e.printStackTrace()}
        }



        if(addressDetailArray.isNotEmpty()){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(addressDetailArray[0].latitude!!,addressDetailArray[0].longitude!!),15f))
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode==1){
            if(ContextCompat.checkSelfPermission(context!!,android.Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){


                val alarm = Context.ALARM_SERVICE
                val am = context!!.getSystemService(alarm) as AlarmManager
                val intent2 = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 12, intent2, 0)
                val type = AlarmManager.RTC
                am.setRepeating(type, System.currentTimeMillis(), 60000, pendingIntent)
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




}