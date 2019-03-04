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

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.support.design.chip.Chip
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getDrawable
import android.util.Pair as UtilPair
import com.clipit.cliptit2.R
import com.clipit.cliptit2.con
import java.text.SimpleDateFormat
import java.util.*
import android.view.*
import android.widget.*

class ExpandableListViewAdapter2(var context: Context, private var expandableListView: ExpandableListView,private var parnetArrayList: MutableList<String>,private var childArray: MutableList<MutableSet<String>>,private  val activity: FragmentActivity) : BaseExpandableListAdapter() {


    private val typefaceCategory = Typeface.createFromAsset(context.assets, "fonts/NotoSerif-Italic.ttf")
    private val typefaceChip = Typeface.createFromAsset(context.assets, "fonts/Slabo27px-Regular.ttf")
    private val newtypefaceDate = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.ttf")
    private val newtypefaceDay = Typeface.createFromAsset(context.assets,"fonts/PlayfairDisplay-Regular.ttf")
    private val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)


    override fun getGroup(groupPosition: Int): String {
        return parnetArrayList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView1 = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val c = Calendar.getInstance().time
        val date = dateFormat.format(c)
        var position = groupPosition

        position = (position + 1) % 6

        var size = childArray[groupPosition].size

        convertView1 = toChangeLayoutByPosition(position, convertView1, inflater, groupPosition, date, size)

        if (expandableListView.isGroupExpanded(groupPosition)) {

            convertView1 = toChangeLayoutByPositionWhenExpand(position, convertView1, inflater, groupPosition, date, size)


        }
        convertView1!!.setOnClickListener {


            if (expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.collapseGroup(groupPosition)
            } else {
                expandableListView.expandGroup(groupPosition)
            }
        }

        return convertView1


    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childArray[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {

        return childArray[groupPosition].elementAt(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView1 = convertView

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView1 = inflater.inflate(R.layout.child_at_home_cardview, null)

        val categoryView = convertView1.findViewById<TextView>(R.id.catergory_home_view)!!
        val imageView = convertView1.findViewById<ImageView>(R.id.image_catergory_view)!!
        categoryView.text = getChild(groupPosition, childPosition).replace('_',' ').toUpperCase()
        categoryView.typeface = typefaceCategory
        val category = getChild(groupPosition, childPosition)




        when (category.toLowerCase()) {




            "atm" -> {
                imageView.setImageResource(R.drawable.ic_outline_atm_24px)
            }
            "bank" -> {
                imageView.setImageResource(R.drawable.ic_outline_account_balance_24px)
            }
            "bar" -> {
                imageView.setImageResource(R.drawable.ic_glass_cocktail)
            }
            "bus_station" -> {
                imageView.setImageResource(R.drawable.ic_bus)
            }
            "bakery" -> {
                imageView.setImageResource(R.drawable.ic_outline_local_cafe_24px)
            }
            "car_wash" -> {
                imageView.setImageResource(R.drawable.ic_car_wash)
            }
            "gas_station" -> {
                imageView.setImageResource(R.drawable.ic_outline_local_gas_station_24px)
            }
            "hospital" -> {
                imageView.setImageResource(R.drawable.ic_outline_hotel_24px)
            }
            "pharmacy" -> {
                imageView.setImageResource(R.drawable.ic_pharmacy)
            }
            "restaurant" -> {
                imageView.setImageResource(R.drawable.ic_silverware_fork_knife)
            }
            "school" -> {
                imageView.setImageResource(R.drawable.ic_outline_school_24px)
            }
            "store" -> {
                imageView.setImageResource(R.drawable.ic_outline_shopping_cart_24px)
            }
            "taxi_station" -> {
                imageView.setImageResource(R.drawable.ic_taxi)
            }
            "train_station" -> {
                imageView.setImageResource(R.drawable.ic_train)
            }
            else -> {
                imageView.setImageResource(R.drawable.ic_outline_search_24px)

            }


        }




        if (isLastChild) {
            convertView1 = View.inflate(context, R.layout.last_child_at_home_cardview, null)

            val categoryView1 = convertView1.findViewById<TextView>(R.id.catergory_home_view_last_child)!!
            val imageView1 = convertView1.findViewById<ImageView>(R.id.image_catergory_view_last_child)!!
            categoryView1.text = getChild(groupPosition, childPosition).replace('_',' ').toUpperCase()
            categoryView1.typeface = typefaceCategory

            when (category.toLowerCase()) {




                "atm" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_atm_24px)
                }
                "bank" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_account_balance_24px)
                }
                "bar" -> {
                    imageView1.setImageResource(R.drawable.ic_glass_cocktail)
                }
                "bus_station" -> {
                    imageView1.setImageResource(R.drawable.ic_bus)
                }
                "bakery" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_local_cafe_24px)
                }
                "car_wash" -> {
                    imageView1.setImageResource(R.drawable.ic_car_wash)
                }
                "gas_station" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_local_gas_station_24px)
                }
                "hospital" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_hotel_24px)
                }
                "pharmacy" -> {
                    imageView1.setImageResource(R.drawable.ic_pharmacy)
                }
                "restaurant" -> {
                    imageView1.setImageResource(R.drawable.ic_silverware_fork_knife)
                }
                "school" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_school_24px)
                }
                "store" -> {
                    imageView1.setImageResource(R.drawable.ic_outline_shopping_cart_24px)
                }
                "taxi_station" -> {
                    imageView1.setImageResource(R.drawable.ic_taxi)
                }
                "train_station" -> {
                    imageView1.setImageResource(R.drawable.ic_train)
                }
                else -> {
                    imageView1.setImageResource(R.drawable.ic_outline_search_24px)

                }


            }
        }

        convertView1!!.setOnClickListener {

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                val intent = Intent(con, ViewCatergoryMain::class.java)
                val array = arrayListOf(parnetArrayList[groupPosition], childArray[groupPosition].elementAt(childPosition))
                intent.putStringArrayListExtra("data", array)
                val option = ActivityOptions.makeSceneTransitionAnimation(activity)
                ContextCompat.startActivity(context,intent,option.toBundle())


            }else{

                val intent = Intent(con, ViewCatergoryMain::class.java)
                val array = arrayListOf(parnetArrayList[groupPosition], childArray[groupPosition].elementAt(childPosition))
                intent.putStringArrayListExtra("data", array)
                ContextCompat.startActivity(context, intent, null)

            }


        }


        return convertView1


    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return parnetArrayList.size
    }

    private fun toChangeLayoutByPosition(position: Int, view: View?, inflater: LayoutInflater, groupPosition: Int, date: String, size: Int): View? {
        var convertView = view

        when (position) {
            0 -> {
                convertView = inflater.inflate(R.layout.parent_card_for_6, null)
                val chipView1 = convertView?.findViewById<Chip>(R.id.parent_card_for_6_chip_1)
                val chipView2 = convertView?.findViewById<Chip>(R.id.parent_card_for_6_chip_2)
                val dateView = convertView?.findViewById<TextView>(R.id.parent_crad_6_text_date)
                val dayView = convertView?.findViewById<TextView>(R.id.parent_card_for_6_text_today)

                dayView!!.text = parnetArrayList[groupPosition]
                dayView.typeface = newtypefaceDate


                if (parnetArrayList[groupPosition] == date) {
                    dayView.text = context.getString(R.string.today)
                    dateView!!.text = date
                }
                dateView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
                dateView.typeface = newtypefaceDay

                for (i in 0..size - 1) {
                    when (i) {
                        0 -> {
                            chipView1!!.visibility = View.VISIBLE
                            when (childArray[groupPosition].elementAt(i).toLowerCase()) {
                                "atm" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_atm_24px)
                                    chipView1.chipText = "ATM"
                                }
                                "bank" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_account_balance_24px)
                                    chipView1.chipText = "BANK"

                                }
                                "bar" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_glass_cocktail)
                                    chipView1.chipText = "BAR"

                                }
                                "bus_station" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_bus)
                                    chipView1.chipText = "BUS STATION"

                                }
                                "bakery" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_local_cafe_24px)
                                    chipView1.chipText = "CAFE"

                                }
                                "car_wash" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_car_wash)
                                    chipView1.chipText = "CAR WASH"

                                }
                                "gas_station" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_local_gas_station_24px)
                                    chipView1.chipText = "GAS STATION"

                                }
                                "hospital" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_hotel_24px)
                                    chipView1.chipText = "HOSPITAL"

                                }
                                "pharmacy" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_pharmacy)
                                    chipView1.chipText = "PHARMACY"

                                }
                                "restaurant" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_silverware_fork_knife)
                                    chipView1.chipText = "RESTAURANT"

                                }
                                "school" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_school_24px)
                                    chipView1.chipText = "SCHOOL"

                                }
                                "store" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_shopping_cart_24px)
                                    chipView1.chipText = "SHOP"

                                }
                                "taxi_station" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_taxi)
                                    chipView1.chipText = "TAXI STATION"

                                }
                                "train_station" -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_train)
                                    chipView1.chipText = "TRAIN STATION"

                                }
                                else -> {
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_search_24px)
                                    chipView1.chipText = "SEARCH"

                                }


                            }


                        }

                        1 -> {
                            chipView2!!.visibility = View.VISIBLE
                            when (childArray[groupPosition].elementAt(i).toLowerCase()) {
                                "atm" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_atm_24px)
                                    chipView2.chipText = "ATM"
                                }
                                "bank" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_account_balance_24px)
                                    chipView2.chipText = "BANK"

                                }
                                "bar" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_glass_cocktail)
                                    chipView2.chipText = "BAR"

                                }
                                "bus_station" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_bus)
                                    chipView2.chipText = "BUS STATION"

                                }
                                "bakery" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_local_cafe_24px)
                                    chipView2.chipText = "CAFE"

                                }
                                "car_wash" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_car_wash)
                                    chipView2.chipText = "CAR WASH"

                                }
                                "gas_station" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_local_gas_station_24px)
                                    chipView2.chipText = "GAS STATION"

                                }
                                "hospital" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_hotel_24px)
                                    chipView2.chipText = "HOSPITAL"

                                }
                                "pharmacy" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_pharmacy)
                                    chipView2.chipText = "PHARMACY"

                                }
                                "restaurant" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_silverware_fork_knife)
                                    chipView2.chipText = "RESTAURANT"

                                }
                                "school" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_school_24px)
                                    chipView2.chipText = "SCHOOL"

                                }
                                "store" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_shopping_cart_24px)
                                    chipView2.chipText = "SHOP"

                                }
                                "taxi_station" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_taxi)
                                    chipView2.chipText = "TAXI STATION"

                                }
                                "train_station" -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_train)
                                    chipView2.chipText = "TRAIN STATION"

                                }
                                else -> {
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_search_24px)
                                    chipView2.chipText = "SEARCH"

                                }


                            }

                        }


                    }

                }
                try {
                    chipView1!!.typeface = typefaceChip
                    chipView2!!.typeface = typefaceChip
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            3 -> {
                convertView = inflater.inflate(R.layout.parent_card_for_3, null)

                val imageView1 = convertView?.findViewById<ImageView>(R.id.parent_today_for_3_image_1)
                val imageView2 = convertView?.findViewById<ImageView>(R.id.parent_today_for_3_image_2)
                val imageView3 = convertView?.findViewById<ImageView>(R.id.parent_today_for_3_image_3)
                val dateView = convertView?.findViewById<TextView>(R.id.parent_card_for_3_text_date)
                val dayView = convertView?.findViewById<TextView>(R.id.parent_card_for_3_text_today)

                dayView!!.text = parnetArrayList[groupPosition]
                dayView.typeface = newtypefaceDate


                if (parnetArrayList[groupPosition] == date) {
                    dayView.text = context.getText(R.string.today)
                    dateView!!.text = date
                }
                dateView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
                dateView.typeface = newtypefaceDay

                for (i in 0..size-1) {
                    when (i) {
                        0 -> {
                            imageView1!!.visibility = View.VISIBLE
                            when (childArray[groupPosition].elementAt(i).toLowerCase()) {
                                "atm" -> {
                                    imageView1.setImageResource(R.drawable.atm)
                                }
                                "bank" -> {
                                    imageView1.setImageResource(R.drawable.bank)
                                }
                                "bar" -> {
                                    imageView1.setImageResource(R.drawable.bar)
                                }
                                "bus_station" -> {
                                    imageView1.setImageResource(R.drawable.bus_station)
                                }
                                "bakery" -> {
                                    imageView1.setImageResource(R.drawable.cafe_new)
                                }
                                "car_wash" -> {
                                    imageView1.setImageResource(R.drawable.car_wash)
                                }
                                "gas_station" -> {
                                    imageView1.setImageResource(R.drawable.gas_station)
                                }
                                "hospital" -> {
                                    imageView1.setImageResource(R.drawable.hospital)
                                }
                                "pharmacy" -> {
                                    imageView1.setImageResource(R.drawable.phramavy)
                                }
                                "restaurant" -> {
                                    imageView1.setImageResource(R.drawable.restaurant)
                                }
                                "school" -> {
                                    imageView1.setImageResource(R.drawable.school)
                                }
                                "store" -> {
                                    imageView1.setImageResource(R.drawable.shops)
                                }
                                "taxi_station" -> {
                                    imageView1.setImageResource(R.drawable.car_wash)
                                }
                                "train_station" -> {
                                    imageView1.setImageResource(R.drawable.train_station)
                                }
                                else -> {
                                    imageView1.setImageResource(R.drawable.cafe)
                                }


                            }

                        }

                        1 -> {


                            imageView2!!.visibility = View.VISIBLE
                            when (childArray[groupPosition].elementAt(i).toLowerCase()) {
                                "atm" -> {
                                    imageView2.setImageResource(R.drawable.atm)
                                }
                                "bank" -> {
                                    imageView2.setImageResource(R.drawable.bank)
                                }
                                "bar" -> {
                                    imageView2.setImageResource(R.drawable.bar)
                                }
                                "bus_station" -> {
                                    imageView2.setImageResource(R.drawable.bus_station)
                                }
                                "cafe" -> {
                                    imageView2.setImageResource(R.drawable.cafe_new)
                                }
                                "car_wash" -> {
                                    imageView2.setImageResource(R.drawable.car_wash)
                                }
                                "gas_station" -> {
                                    imageView2.setImageResource(R.drawable.gas_station)
                                }
                                "hospital" -> {
                                    imageView2.setImageResource(R.drawable.hospital)
                                }
                                "pharmacy" -> {
                                    imageView2.setImageResource(R.drawable.phramavy)
                                }
                                "restaurant" -> {
                                    imageView2.setImageResource(R.drawable.restaurant)
                                }
                                "school" -> {
                                    imageView2.setImageResource(R.drawable.school)
                                }
                                "store" -> {
                                    imageView2.setImageResource(R.drawable.shops)
                                }
                                "taxi_station" -> {
                                    imageView2.setImageResource(R.drawable.car_wash)
                                }
                                "train_station" -> {
                                    imageView2.setImageResource(R.drawable.train_station)
                                }
                                else -> {
                                    imageView2.setImageResource(R.drawable.cafe)
                                }


                            }

                        }
                        2 -> {
                            imageView3!!.visibility = View.VISIBLE
                            when (childArray[groupPosition].elementAt(i).toLowerCase()) {
                                "atm" -> {
                                    imageView3.setImageResource(R.drawable.atm)
                                }
                                "bank" -> {
                                    imageView3.setImageResource(R.drawable.bank)
                                }
                                "bar" -> {
                                    imageView3.setImageResource(R.drawable.bar)
                                }
                                "bus_station" -> {
                                    imageView3.setImageResource(R.drawable.bus_station)
                                }
                                "bakery" -> {
                                    imageView3.setImageResource(R.drawable.cafe_new)
                                }
                                "car_wash" -> {
                                    imageView3.setImageResource(R.drawable.car_wash)
                                }
                                "gas_station" -> {
                                    imageView3.setImageResource(R.drawable.gas_station)
                                }
                                "hospital" -> {
                                    imageView3.setImageResource(R.drawable.hospital)
                                }
                                "pharmacy" -> {
                                    imageView3.setImageResource(R.drawable.phramavy)
                                }
                                "restaurant" -> {
                                    imageView3.setImageResource(R.drawable.restaurant)
                                }
                                "school" -> {
                                    imageView3.setImageResource(R.drawable.school)
                                }
                                "store" -> {
                                    imageView3.setImageResource(R.drawable.shops)
                                }
                                "taxi_station" -> {
                                    imageView3.setImageResource(R.drawable.car_wash)
                                }
                                "train_station" -> {
                                    imageView3.setImageResource(R.drawable.train_station)
                                }
                                else -> {
                                    imageView3.setImageResource(R.drawable.cafe)
                                }


                            }


                        }

                    }
                }
            }

            else -> {
                convertView = inflater.inflate(R.layout.parent_card_for_else, null)
                val dateView = convertView?.findViewById<TextView>(R.id.date_home_list_view_first_layout)
                val dayView = convertView?.findViewById<TextView>(R.id.date_of_day_home_list_view_first_layout)

                dateView!!.text = parnetArrayList[groupPosition]
                dateView.typeface = newtypefaceDate


                if (parnetArrayList[groupPosition] == date) {
                    dateView.text = context.getText(R.string.today)
                    dayView!!.text = date
                }
                dayView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
                dayView.typeface = newtypefaceDay


            }


        }

        if (parnetArrayList[groupPosition] == date) {
            convertView = inflater.inflate(R.layout.parent_card_today, null)
            val imageView1 = convertView?.findViewById<ImageView>(R.id.parent_today_image_1)

            val dateView = convertView?.findViewById<TextView>(R.id.parent_text_date)
            val dayView = convertView?.findViewById<TextView>(R.id.parent_text_today)

            dateView!!.text = parnetArrayList[groupPosition]
            dateView.typeface = newtypefaceDate

            if (parnetArrayList[groupPosition] == date) {
                dayView!!.text = context.getText(R.string.today)
                dateView.text = date
            }
            else {
                dayView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
            }
            dayView.typeface = newtypefaceDay

            for (i in 0..size-1) {
                when (i) {
                    0 -> {
                        imageView1!!.visibility = View.VISIBLE
                        when (childArray[groupPosition].elementAt(i).toLowerCase()) {
                            "atm" -> {
                                imageView1.setImageResource(R.drawable.atm)
                            }
                            "bank" -> {
                                imageView1.setImageResource(R.drawable.bank)
                            }
                            "bar" -> {
                                imageView1.setImageResource(R.drawable.bar)
                            }
                            "bus_station" -> {
                                imageView1.setImageResource(R.drawable.bus_station)
                            }
                            "bakery" -> {
                                imageView1.setImageResource(R.drawable.cafe_new)
                            }
                            "car_wash" -> {
                                imageView1.setImageResource(R.drawable.car_wash)
                            }
                            "gas_station" -> {
                                imageView1.setImageResource(R.drawable.gas_station)
                            }
                            "hospital" -> {
                                imageView1.setImageResource(R.drawable.hospital)
                            }
                            "pharmacy" -> {
                                imageView1.setImageResource(R.drawable.phramavy)
                            }
                            "restaurant" -> {
                                imageView1.setImageResource(R.drawable.restaurant)
                            }
                            "school" -> {
                                imageView1.setImageResource(R.drawable.school)
                            }
                            "store" -> {
                                imageView1.setImageResource(R.drawable.shops)
                            }
                            "taxi_station" -> {
                                imageView1.setImageResource(R.drawable.car_wash)
                            }
                            "train_station" -> {
                                imageView1.setImageResource(R.drawable.train_station)
                            }
                            else -> {
                                imageView1.setImageResource(R.drawable.cafe)
                            }


                        }

                    }

                }

            }

        }



        return convertView
    }


    private fun toChangeLayoutByPositionWhenExpand(position: Int ,view: View?,inflater: LayoutInflater,groupPosition: Int,date : String,size:Int) :View? {
        var convertView = view

        when(position){
            0->{
                convertView = inflater.inflate(R.layout.parent_expand_card_for_6,null)

                val chipView1 =  convertView?.findViewById<Chip>(R.id.parent_card_for_6_chip_1)
                val chipView2 = convertView?.findViewById<Chip>(R.id.parent_card_for_6_chip_2)
                val dateView = convertView?.findViewById<TextView>(R.id.parent_crad_6_text_date)
                val dayView = convertView?.findViewById<TextView>(R.id.parent_card_for_6_text_today)

                dayView!!.text = parnetArrayList[groupPosition]
                dayView.typeface = newtypefaceDate


                if (parnetArrayList[groupPosition] == date) {
                    dayView.text = context.getText(R.string.today)
                    dateView!!.text = date
                }
                dateView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
                dateView.typeface = newtypefaceDay

                for ( i in 0..size-1){
                    when(i){
                        0->{chipView1!!.visibility = View.VISIBLE
                            when(childArray[groupPosition].elementAt(i).toLowerCase()){
                                "atm"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_atm_24px)
                                    chipView1.chipText = "ATM"
                                }
                                "bank"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_account_balance_24px)
                                    chipView1.chipText = "BANK"

                                }
                                "bar"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_glass_cocktail)
                                    chipView1.chipText = "BAR"

                                }
                                "bus_station"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_bus)
                                    chipView1.chipText = "BUS STATION"

                                }
                                "bakery"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_local_cafe_24px)
                                    chipView1.chipText = "BAKERY"

                                }
                                "car_wash"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_car_wash)
                                    chipView1.chipText = "CAR WASH"

                                }
                                "gas_station"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_local_gas_station_24px)
                                    chipView1.chipText = "GAS STATION"

                                }
                                "hospital"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_hotel_24px)
                                    chipView1.chipText = "HOSPITAL"

                                }
                                "pharmacy"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_pharmacy)
                                    chipView1.chipText = "PHARMACY"

                                }
                                "restaurant"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_silverware_fork_knife)
                                    chipView1.chipText = "RESTAURANT"

                                }
                                "school"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_school_24px)
                                    chipView1.chipText = "SCHOOL"

                                }
                                "store"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_shopping_cart_24px)
                                    chipView1.chipText = "STORE"

                                }
                                "taxi_station"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_taxi)
                                    chipView1.chipText = "TAXI STATION"

                                }
                                "train_station"->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_train)
                                    chipView1.chipText = "TRAIN STATION"

                                }
                                else->{
                                    chipView1.chipIcon = getDrawable(con, R.drawable.ic_outline_search_24px)
                                    chipView1.chipText = "SEARCH"

                                }


                            }



                        }

                        1->{chipView2!!.visibility = View.VISIBLE
                            when(childArray[groupPosition].elementAt(i).toLowerCase()){
                                "atm"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_atm_24px)
                                    chipView2.chipText = "ATM"
                                }
                                "bank"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_account_balance_24px)
                                    chipView2.chipText = "BANK"

                                }
                                "bar"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_glass_cocktail)
                                    chipView2.chipText = "BAR"

                                }
                                "bus_station"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_bus)
                                    chipView2.chipText = "BUS STATION"

                                }
                                "bakery"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_local_cafe_24px)
                                    chipView2.chipText = "BAKERY"

                                }
                                "car_wash"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_car_wash)
                                    chipView2.chipText = "CAR WASH"

                                }
                                "gas_station"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_local_gas_station_24px)
                                    chipView2.chipText = "GAS STATION"

                                }
                                "hospital"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_hotel_24px)
                                    chipView2.chipText = "HOSPITAL"

                                }
                                "pharmacy"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_pharmacy)
                                    chipView2.chipText = "PHARMACY"

                                }
                                "restaurant"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_silverware_fork_knife)
                                    chipView2.chipText = "RESTAURANT"

                                }
                                "school"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_school_24px)
                                    chipView2.chipText = "SCHOOL"

                                }
                                "store"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_shopping_cart_24px)
                                    chipView2.chipText = "STORE"

                                }
                                "taxi_station"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_taxi)
                                    chipView2.chipText = "TAXI STATION"

                                }
                                "train_station"->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_train)
                                    chipView2.chipText = "TRAIN STATION"

                                }
                                else->{
                                    chipView2.chipIcon = getDrawable(con, R.drawable.ic_outline_search_24px)
                                    chipView2.chipText = "SEARCH"

                                }


                            }

                        }


                    }

                }
            }
            3->{
                convertView = inflater.inflate(R.layout.parent_expand_card_for_3,null)

                val imageView1 =  convertView?.findViewById<ImageView>(R.id.parent_today_for_3_image_1)
                val imageView2 = convertView?.findViewById<ImageView>(R.id.parent_today_for_3_image_2)
                val imageView3 = convertView?.findViewById<ImageView>(R.id.parent_today_for_3_image_3)
                val dateView = convertView?.findViewById<TextView>(R.id.parent_card_for_3_text_date)
                val dayView = convertView?.findViewById<TextView>(R.id.parent_card_for_3_text_today)

                dayView!!.text = parnetArrayList[groupPosition]
                dayView.typeface = newtypefaceDate


                if (parnetArrayList[groupPosition] == date) {
                    dayView.text = context.getText(R.string.today)
                    dateView!!.text = date
                }
                dateView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
                dateView.typeface = newtypefaceDay


                for ( i in 0..size-1){
                    when(i){
                        0->{imageView1!!.visibility = View.VISIBLE
                            when(childArray[groupPosition].elementAt(i).toLowerCase()){
                                "atm"->{
                                    imageView1.setImageResource(R.drawable.atm)
                                }
                                "bank"->{
                                    imageView1.setImageResource(R.drawable.bank)
                                }
                                "bar"->{
                                    imageView1.setImageResource(R.drawable.bar)
                                }
                                "bus_station"->{
                                    imageView1.setImageResource(R.drawable.bus_station)
                                }
                                "bakery"->{
                                    imageView1.setImageResource(R.drawable.cafe_new)
                                }
                                "car_wash"->{
                                    imageView1.setImageResource(R.drawable.car_wash)
                                }
                                "gas_station"->{
                                    imageView1.setImageResource(R.drawable.gas_station)
                                }
                                "hospital"->{
                                    imageView1.setImageResource(R.drawable.hospital)
                                }
                                "pharmacy"->{
                                    imageView1.setImageResource(R.drawable.phramavy)
                                }
                                "restaurant"->{
                                    imageView1.setImageResource(R.drawable.restaurant)
                                }
                                "school"->{
                                    imageView1.setImageResource(R.drawable.school)
                                }
                                "store"->{
                                    imageView1.setImageResource(R.drawable.shops)
                                }
                                "taxi_station"->{
                                    imageView1.setImageResource(R.drawable.car_wash)
                                }
                                "train_station"->{
                                    imageView1.setImageResource(R.drawable.train_station)
                                }
                                else->{
                                    imageView1.setImageResource(R.drawable.cafe)
                                }


                            }

                        }
                        1->{imageView2!!.visibility = View.VISIBLE
                            when(childArray[groupPosition].elementAt(i).toLowerCase()){
                                "atm"->{
                                    imageView2.setImageResource(R.drawable.atm)
                                }
                                "bank"->{
                                    imageView2.setImageResource(R.drawable.bank)
                                }
                                "bar"->{
                                    imageView2.setImageResource(R.drawable.bar)
                                }
                                "bus_station"->{
                                    imageView2.setImageResource(R.drawable.bus_station)
                                }
                                "bakery"->{
                                    imageView2.setImageResource(R.drawable.cafe_new)}
                                "car_wash"->{
                                    imageView2.setImageResource(R.drawable.car_wash)
                                }
                                "gas_station"->{
                                    imageView2.setImageResource(R.drawable.gas_station)
                                }
                                "hospital"->{
                                    imageView2.setImageResource(R.drawable.hospital)
                                }
                                "pharmacy"->{
                                    imageView2.setImageResource(R.drawable.phramavy)
                                }
                                "restaurant"->{
                                    imageView2.setImageResource(R.drawable.restaurant)
                                }
                                "school"->{
                                    imageView2.setImageResource(R.drawable.school)
                                }
                                "store"->{
                                    imageView2.setImageResource(R.drawable.shops)
                                }
                                "taxi_station"->{
                                    imageView2.setImageResource(R.drawable.car_wash)
                                }
                                "train_station"->{
                                    imageView2.setImageResource(R.drawable.train_station)
                                }
                                else->{
                                    imageView2.setImageResource(R.drawable.cafe)
                                }


                            }

                        }
                        2->{imageView3!!.visibility = View.VISIBLE
                            when(childArray[groupPosition].elementAt(i).toLowerCase()){
                                "atm"->{
                                    imageView3.setImageResource(R.drawable.atm)
                                }
                                "bank"->{
                                    imageView3.setImageResource(R.drawable.bank)
                                }
                                "bar"->{
                                    imageView3.setImageResource(R.drawable.bar)
                                }
                                "bus_station"->{
                                    imageView3.setImageResource(R.drawable.bus_station)
                                }
                                "bakery"->{
                                    imageView3.setImageResource(R.drawable.cafe_new)
                                }
                                "car_wash"->{
                                    imageView3.setImageResource(R.drawable.car_wash)
                                }
                                "gas_station"->{
                                    imageView3.setImageResource(R.drawable.gas_station)
                                }
                                "hospital"->{
                                    imageView3.setImageResource(R.drawable.hospital)
                                }
                                "pharmacy"->{
                                    imageView3.setImageResource(R.drawable.phramavy)
                                }
                                "restaurant"->{
                                    imageView3.setImageResource(R.drawable.restaurant)
                                }
                                "school"->{
                                    imageView3.setImageResource(R.drawable.school)
                                }
                                "store"->{
                                    imageView3.setImageResource(R.drawable.shops)
                                }
                                "taxi_station"->{
                                    imageView3.setImageResource(R.drawable.car_wash)
                                }
                                "train_station"->{
                                    imageView3.setImageResource(R.drawable.train_station)
                                }
                                else->{
                                    imageView3.setImageResource(R.drawable.cafe)
                                }


                            }

                        }

                    }
                }



            }

            else->{
                convertView = inflater.inflate(R.layout.parent_expand_card_for_else,null)
                val dateView = convertView?.findViewById<TextView>(R.id.date_home_list_view_second_layout)
                val dayView = convertView?.findViewById<TextView>(R.id.date_of_day_home_list_view_second_layout)

                dateView!!.text = parnetArrayList[groupPosition]
                dateView.typeface = newtypefaceDate


                if (parnetArrayList[groupPosition] == date) {
                    dateView.text = context.getText(R.string.today)
                    dayView!!.text = date
                }
                dayView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
                dayView.typeface = newtypefaceDay



            }




        }

        if(parnetArrayList[groupPosition]==date){
            convertView = inflater.inflate(R.layout.parent_expand_card_for_today,null)
            val imageView1 = convertView?.findViewById<ImageView>(R.id.parent_today_image_1)

            val dateView = convertView?.findViewById<TextView>(R.id.parent_text_date)
            val dayView = convertView?.findViewById<TextView>(R.id.parent_text_today)

            dateView!!.text = parnetArrayList[groupPosition]
            dateView.typeface = newtypefaceDate


            if (parnetArrayList[groupPosition] == date) {
                dayView!!.text = context.getText(R.string.today)
                dateView.text = date
            }
            else {
                dayView!!.text = dateIs(dateFormat.parse(parnetArrayList[groupPosition]).day)
            }
            dayView.typeface = newtypefaceDay



            for ( i in 0..size-1){
                when(i){
                    0->{imageView1!!.visibility = View.VISIBLE
                        when(childArray[groupPosition].elementAt(i).toLowerCase()){
                            "atm"->{
                                imageView1.setImageResource(R.drawable.atm)
                            }
                            "bank"->{
                                imageView1.setImageResource(R.drawable.bank)
                            }
                            "bar"->{
                                imageView1.setImageResource(R.drawable.bar)
                            }
                            "bus_station"->{
                                imageView1.setImageResource(R.drawable.bus_station)
                            }
                            "bakery"->{
                                imageView1.setImageResource(R.drawable.cafe_new)
                            }
                            "car_wash"->{
                                imageView1.setImageResource(R.drawable.car_wash)
                            }
                            "gas_station"->{
                                imageView1.setImageResource(R.drawable.gas_station)
                            }
                            "hospital"->{
                                imageView1.setImageResource(R.drawable.hospital)
                            }
                            "pharmacy"->{
                                imageView1.setImageResource(R.drawable.phramavy)
                            }
                            "restaurant"->{
                                imageView1.setImageResource(R.drawable.restaurant)
                            }
                            "school"->{
                                imageView1.setImageResource(R.drawable.school)
                            }
                            "store"->{
                                imageView1.setImageResource(R.drawable.shops)
                            }
                            "taxi_station"->{
                                imageView1.setImageResource(R.drawable.car_wash)
                            }
                            "train_station"->{
                                imageView1.setImageResource(R.drawable.train_station)
                            }
                            else->{
                                imageView1.setImageResource(R.drawable.cafe)
                            }


                        }

                    }

                }

            }

        }


        return convertView
    }


    private fun dateIs(day : Int) : String{
        var dayis=""
        when(day){
            0->dayis= "Sunday"
            1->dayis = "Monday"
            2->dayis="Tuesday"
            3->dayis = "Wednesday"
            4->dayis =  "Thursday"
            5->dayis =  "Friday"
            6->dayis =  "Saturday"
        }
        return dayis
    }



}