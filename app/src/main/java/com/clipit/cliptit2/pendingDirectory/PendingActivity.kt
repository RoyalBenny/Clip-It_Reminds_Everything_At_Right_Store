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
package com.clipit.cliptit2.pendingDirectory

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ExpandableListView
import com.clipit.cliptit2.backgroundDirectory.ForeGroundKotlin
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.globalVariablesDirectory.ItemClass
import com.clipit.cliptit2.R

class PendingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending)

        val idArray = intent.getStringArrayListExtra("idArray").toSet()
        val db = JsonDataBase(this)
        val data :ArrayList<ArrayList<ItemClass>> = ArrayList()
        val parent = ArrayList<String>()

        idArray.forEach{
            data.add(db.returnItemBasedOnId(it.toInt()))
            parent.add(db.returnCategoryBasedOnId(it.toInt())!!)
        }

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            val windows: Window = this.window
            windows.statusBarColor = this.getColor(R.color.material_new_blue_strong_200)
        }

        val expand = findViewById<ExpandableListView>(R.id.expandable_list_view_at_pending_intent)
        expand.setAdapter(PendingActivityExpandListAdapterClass(this, expand, parent, data))

    }

    override fun onBackPressed() {

        try {
            if (isMyServiceRunning(this, ForeGroundKotlin::class.java)) {
                val intent2 = Intent(this, ForeGroundKotlin::class.java)
                stopService(intent2)
            }

        }catch (e:Exception){e.printStackTrace()}
        super.onBackPressed()
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

