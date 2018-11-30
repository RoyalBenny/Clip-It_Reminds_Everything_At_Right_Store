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
package com.clipit.cliptit2.globalVariablesDirectory

class ShopClass {

    var latitude:Double? = null
    var longitude:Double? = null
    var shopName : String? = null
    var date : String? = null
    var shopCategory : String? = null
    var shopLocation : String? =null
    var id : Int? =null
    var delete :Int? = null
    var brought : Int? = null
    var archive : Int? = null
    var specfied: Int? = null

    constructor(latitude:Double,longitude : Double, shopName : String , date : String, shopCategory : String, shopLocation : String, id : Int,delete:Int,brought:Int,archive:Int,specified:Int){

        this.latitude = latitude
        this.longitude = longitude
        this.shopName = shopName
        this.date = date
        this.shopCategory = shopCategory
        this.shopLocation = shopLocation
        this.id = id
        this.delete = delete
        this.brought = brought
        this.archive = archive
        this.specfied = specified

    }
    constructor()
}