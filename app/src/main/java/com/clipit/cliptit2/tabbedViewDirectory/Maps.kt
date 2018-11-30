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

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.clipit.cliptit2.R
import com.clipit.cliptit2.con
import com.clipit.cliptit2.databaseDirectory.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.collections.ArrayList

class Maps : Fragment() , OnMapReadyCallback , AdapterView.OnItemSelectedListener {


    private lateinit var mMap: GoogleMap
    private var locationListener: LocationListener? = null
    private var locationManager: LocationManager? = null
    private var addressDetailArray = arrayListOf<ShopClass>()
    private var userLastLocation: Location? = null
    private val category: ArrayList<String> = ArrayList()
    private val db = JsonDataBase(con)
    private var firstTimeLaunch = true
    private lateinit var userLocation : LatLng
    private var idForMap :Int? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = view.findViewById<MapView>(R.id.map_view_at_bottom_sheet)
        val spinner = view.findViewById<Spinner>(R.id.Spinner_for_bottom_sheet)

        Thread(Runnable {

            mapFragment.post {
                if (mapFragment != null) {
                    mapFragment.onCreate(null)
                    mapFragment.onResume()
                    mapFragment.getMapAsync(this)
                }
            }

        }).start()


        try {
            nearShopMap.keys.forEach {
                category.add(it)
            }


            spinner.adapter = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item, category)
            if (spinner.adapter.isEmpty) spinner.visibility = View.GONE
            spinner.onItemSelectedListener = this


        } catch (e: Exception) {
            e.printStackTrace()
        }



        super.onViewCreated(view, savedInstanceState)


    }

    fun addShopMarker() {




        addressDetailArray.forEach {

            if(it.specfied==0)
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude!!.toDouble(), it.longitude!!.toDouble())).title(it.shopName))
            else
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude!!.toDouble(), it.longitude!!.toDouble())).title(it.shopName)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        }
        if (addressDetailArray.isEmpty()) return
        val latLng = LatLng(addressDetailArray[0].latitude!!.toDouble(), addressDetailArray[0].longitude!!.toDouble())
        if(firstTimeLaunch) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            firstTimeLaunch = false
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        mMap.clear()

        try {
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        }catch (e:Exception){e.printStackTrace()}
        Toast.makeText(context,"Double click the marker to specify it",Toast.LENGTH_SHORT).show()
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                if (p0 != null) {
                    mMap.clear()
                    userLocation = LatLng(p0.latitude, p0.longitude)
                    userLastLocation = p0
                    mMap.addMarker(MarkerOptions().title("Your Location").position(userLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

                    if(firstTimeLaunch){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                        firstTimeLaunch = false

                    }
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
        var seleted =""
        mMap.setOnInfoWindowClickListener {


            try {

                if(it.title != seleted){
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
                        }                    }

                }


            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        mMap.setOnMarkerClickListener {

            try {

                if (it.title != seleted){
                    seleted = it.title
                }

            else {
                    val shop = addressDetailArray.filter { ki ->
                        it.title == ki.shopName
                    }

                    if (shop[0].specfied == 0) {
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
                    } else {
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
                        }                    }

                }


            } catch (
                    e: Exception
            ) {
                e.printStackTrace()
            }
            return@setOnMarkerClickListener false
        }



            try {


            if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

            } else {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2f, locationListener)
                val userLastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                try {
                    val latLng = LatLng(userLastLocation.latitude, userLastLocation.longitude)
                    mMap.addCircle(CircleOptions().center(latLng))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3f))
                    addShopMarker()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }catch (e:Exception){e.printStackTrace()}

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2f, locationListener)

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        addressDetailArray.clear()
        idForMap = nearShopMap[category[position]]
        addressDetailArray.addAll(db.readJsonFromId(idForMap!!))
        try {
            mMap.clear()
            addShopMarker()
            mMap.addMarker(MarkerOptions().title("Your Location").position(LatLng(userLastLocation!!.latitude,userLastLocation!!.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        }catch (e:Exception){e.printStackTrace()}
    }
}