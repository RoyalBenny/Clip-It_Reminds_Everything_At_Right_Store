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
package com.clipit.cliptit2.createDirectory

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.clipit.cliptit2.*
import com.clipit.cliptit2.backgroundDirectory.JsonDownloadAsync
import com.clipit.cliptit2.globalVariablesDirectory.GlobalCategory
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.fragment_create_new_.*
import java.util.*
import kotlin.collections.ArrayList

class CreateActivity : AppCompatActivity() {





    var date :String =""
    private lateinit var placeAutocompleteAdapter : PlaceAutocompleteAdapter
    private lateinit var mGeoDataClient : GeoDataClient
    private lateinit var latLngBounds: LatLngBounds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_create_new_)
        setSupportActionBar(toolbar_create)
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(R.color.material_new_brown_strong_10_transparent)
        }

        toolbar_create.setNavigationOnClickListener {
           onBackPressed()
        }

        val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val calender = Calendar.getInstance()
        latLngBounds = LatLngBounds(
                LatLng((-40).toDouble(), (-168).toDouble()) , LatLng(71.toDouble(),136.toDouble())
        )
        mGeoDataClient = Places.getGeoDataClient(this, null)
        placeAutocompleteAdapter = PlaceAutocompleteAdapter(this, mGeoDataClient, latLngBounds, null)
        date = dateFormat.format(calender.time)
        create_select_date_button.text = date

            create_select_date_button.setOnClickListener {

               val dpt = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                   selectedDate.set(Calendar.YEAR, year)
                  selectedDate.set(Calendar.MONTH, month)
                   selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                   date = dateFormat.format(selectedDate.time)
                   create_select_date_button.text = date

                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH))
                dpt.datePicker.minDate = System.currentTimeMillis()-1000
              dpt.show()

            }


                view_at_create_bottom_app_bar.startAnimation(AnimationUtils.loadAnimation(this,R.anim.bottom_app_bar_appear))
                create_bottom_app_bar.startAnimation(AnimationUtils.loadAnimation(this,R.anim.bottom_app_bar_appear))
                val adapterRec = ArrayList<ShopClass>()
                adapterRec.add(ShopClass())
                GlobalCategory.arrayGlobalCategory = adapterRec
                val recyclerView = findViewById<RecyclerView>(R.id.recyceler_view_create_activity)
                recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                recyclerView.adapter = CategoryClassAdapter(GlobalCategory.returnGlobalCategory(), this.placeAutocompleteAdapter)





                create_new_category_button.setOnClickListener {

                    if(GlobalCategory.arrayGlobalCategory.lastIndex+1==3){
                        Snackbar.make(create_new_category_button,"Reach the limit",Snackbar.LENGTH_LONG).show()
                    }
                    else {


                        GlobalCategory.arrayGlobalCategory.add(ShopClass())
                        recyclerView.adapter!!.notifyItemInserted(GlobalCategory.arrayGlobalCategory.lastIndex + 1)
                    }
                }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

        }



            }





    override fun onBackPressed() {

        if (date.isNotEmpty()) {
            val data = JsonDownloadAsync(date, this, 1)
            data.execute()
        }


        view_at_create_bottom_app_bar.startAnimation(AnimationUtils.loadAnimation(this,R.anim.bottom_app_bar_disappear))
        create_bottom_app_bar.startAnimation(AnimationUtils.loadAnimation(this,R.anim.bottom_app_bar_disappear))

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        super.onBackPressed()


    }





}

