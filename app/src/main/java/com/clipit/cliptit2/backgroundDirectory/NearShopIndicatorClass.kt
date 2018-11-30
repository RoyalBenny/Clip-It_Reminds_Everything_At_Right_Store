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
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.google.android.gms.maps.model.LatLng
import kotlin.math.pow
import kotlin.math.sqrt

class NearShopIndicatorClass(var context: Context, private var idArray : ArrayList<ShopClass>, var number:Int)  {
    private var distanceValue  : Double?=null

    fun shopIndicator(latLng: LatLng, arrayGlobalJson: ArrayList<ShopClass>): List<DistanceStoreClass> {
//To do near the location
        distanceValue = distance(number)
        val id : ArrayList<DistanceStoreClass> = arrayListOf()

            arrayGlobalJson.forEach {
                val distance = returnDistance(latLng, LatLng(it.latitude!!,it.longitude!!))
                if(distance<=distanceValue!!){
                    id.add(DistanceStoreClass(it.id!!, distance, it.latitude!!, it.longitude!!))

                }


            }
                return id

    }

    fun placeIndicator(latLng: LatLng): List<DistanceStoreClass> {
//To do near the location
        val id : ArrayList<DistanceStoreClass> = arrayListOf()

        idArray.forEach {

            val distance = returnDistance(latLng, LatLng(it.latitude!!,it.longitude!!))
            if(distance<=0.0138){
                id.add(DistanceStoreClass(it.id!!, distance, it.latitude!!, it.longitude!!))
            }

        }

        return id

    }


    fun placeIndicatorForNearToShop(latLng: LatLng): List<DistanceStoreClass> {
//To do near the location
        val id : ArrayList<DistanceStoreClass> = arrayListOf()

        idArray.forEach {

            val distance = returnDistance(latLng, LatLng(it.latitude!!,it.longitude!!))
            if(distance<=0.0138){
                id.add(DistanceStoreClass(it.id!!, distance, it.latitude!!, it.longitude!!))

            }

        }
        return id.sortedBy {
            it.distance
        }

    }


    fun shopIndicatorNearToShop(latLng: LatLng, arrayGlobalJson: ArrayList<ShopClass>): MutableSet<Int> {
//To do near the location
        val id : ArrayList<DistanceStoreClass> = arrayListOf()

            arrayGlobalJson.forEach {
                distanceValue = distance(number)
                val distance = returnDistance(latLng, LatLng(it.latitude!!, it.longitude!!))
                if (distance <= distanceValue!!) {
                    id.add(DistanceStoreClass(it.id!!, distance, it.latitude!!, it.longitude!!))

                }

            }

        id.sortBy {
            it.distance
        }

        val hashMap : MutableSet<Int> = HashSet()

        id.forEach {
            hashMap.add(it.id!!)
        }

        return hashMap
    }




    private fun returnDistance(userLocation:LatLng, shopLocation: LatLng): Double {
        return sqrt((userLocation.latitude-shopLocation.latitude).pow(2) + (userLocation.longitude - shopLocation.longitude).pow(2))
    }

    private fun distance(dist:Int):Double{
        return 7.88*(10.toDouble().pow(-6))*dist
    }
}