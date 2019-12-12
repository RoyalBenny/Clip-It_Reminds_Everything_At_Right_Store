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


class DistanceStoreClass {

    var id :Int? =null
    var distance :Double? =null
    var latitude : Double? =null
    var longitude : Double? =null


    constructor( id : Int, distance :Double,latitude:Double , longitude :Double){


        this.id = id
        this.distance = distance
        this.latitude = latitude
        this.longitude = longitude

    }
    constructor()

}