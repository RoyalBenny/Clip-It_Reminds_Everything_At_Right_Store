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
import android.os.AsyncTask
import androidx.work.*
import androidx.work.Data
import com.clipit.cliptit2.globalVariablesDirectory.GlobalCategory
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.createDirectory.itemName2
import com.clipit.cliptit2.homeDirectory.itemName3


class JsonDownloadAsync(var date:String, val context: Context, private var case:Int) :AsyncTask<String,Void,String>() {

    override fun doInBackground(vararg params: String?): String {





        var i =0


        GlobalCategory.returnGlobalCategory().forEach{

            val db = JsonDataBase(context)
            if(it.shopLocation==null || it.shopCategory==null) {
                i++
                return@forEach}
            val id = db.insertAutoNew(date,it.shopLocation!!,it.shopCategory!!,0.0,0.0,0)

            val workData:Data  = mapOf("location" to it.shopLocation,"category" to it.shopCategory ,"date" to date,"id" to id.toString()).toWorkData()
            val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val background = OneTimeWorkRequest.Builder(JsonDataBaseBackGroundProcess::class.java).setConstraints(constraints).setInputData(workData).build()
            WorkManager.getInstance().enqueue(background)
            if(case == 1) {
                itemName2[i].split('\n').forEach { pi ->
                    db.insertItem(pi, id, 0)



                }
            }else {

                itemName3[i].split('\n').forEach { pi ->
                    db.insertItem(pi, id, 0)


                }
            }


            i++
        }




        return  "done"
    }
}