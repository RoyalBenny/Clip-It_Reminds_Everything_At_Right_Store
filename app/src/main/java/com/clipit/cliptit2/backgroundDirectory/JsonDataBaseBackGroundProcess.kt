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

import android.content.Context
import android.location.Geocoder
import androidx.work.Worker
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.clipit.cliptit2.con
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class JsonDataBaseBackGroundProcess(val context: Context): Worker() {

    constructor():this(con)

    var shopLocation = ""
    var shopCategory = ""
    var date = ""
    var id = -1
    private val shopArray = ArrayList<ShopClass>()

    override fun doWork(): Result {



        return Result.SUCCESS
    }




    private fun getLocationLatLng(location: String): LatLng {
        val geocoder = Geocoder(context)
        var lat = 0.0
        var lng = 0.0
        for(i in 0..7){
        try {
            val data = geocoder.getFromLocationName(location, 5)

            data.forEach {
                lat = it.latitude
                lng = it.longitude
            }
            val db = JsonDataBase(context)

            db.updateAuto(id, shopCategory, date, shopLocation, lat, lng)
            Thread.sleep(20)

        }catch (e:Exception){
        e.printStackTrace()}
        }

        return LatLng(lat, lng)

    }

    private fun getJson(googleUrl: String) : Boolean {


        var result = ""
        val url= URL(googleUrl)
        val httpURLConnection: HttpURLConnection
        httpURLConnection = url.openConnection() as HttpURLConnection
        val inputStream = httpURLConnection.inputStream
        val inputStreamReader = InputStreamReader(inputStream)
        var data = inputStreamReader.read()
        while (data > 0) {

            val character = data.toChar()
            result += character

            data = inputStreamReader.read()
        }
        val jsonObject = JSONObject(result)
        if(jsonObject.getString("status")=="ZERO_RESULTS") {
            return false}
        val base = jsonObject.getJSONArray("results")

        for (i in 0..(base.length() - 1)) {
            val lat = base.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat").toString().toDouble()
            val lng = base.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng").toString().toDouble()
            val tempShop = ShopClass()
            tempShop.longitude = lng
            tempShop.latitude = lat
            tempShop.shopCategory = ""
            tempShop.date = ""
            tempShop.shopName = base.getJSONObject(i).getString("name").toString()
            shopArray.add(tempShop)

        }
        return true



    }


}