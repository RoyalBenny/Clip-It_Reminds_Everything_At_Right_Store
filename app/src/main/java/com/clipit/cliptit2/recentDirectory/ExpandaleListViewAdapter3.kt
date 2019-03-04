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
package com.clipit.cliptit2.recentDirectory

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import com.clipit.cliptit2.R
import com.clipit.cliptit2.con
import java.text.SimpleDateFormat
import java.util.*

class ExpandableListViewAdapter3(var context: Context,private var expandableListView: ExpandableListView,private var parnetArrayList: MutableList<String>,private var childArray: MutableList<MutableSet<String>>,private val activity: FragmentActivity) : BaseExpandableListAdapter()  {

    private val typefaceCategory = Typeface.createFromAsset(context.assets,"fonts/NotoSerif-Italic.ttf")
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
        var position  = groupPosition

        position = (position+1)%6

        val size = childArray[groupPosition].size

        convertView1 =  toChangeLayoutByPosition(position,convertView1,inflater,groupPosition,date,size)






        if(expandableListView.isGroupExpanded(groupPosition)) {
            convertView1 = toChangeLayoutByPositionWhenExpand(position,convertView1,inflater,groupPosition,date,size)

        }
        convertView1!!.setOnClickListener {
            if(expandableListView.isGroupExpanded(groupPosition)){
                expandableListView.collapseGroup(groupPosition)
            }
            else {
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

        val categoryView = convertView1?.findViewById<TextView>(R.id.catergory_home_view)
        val imageView = convertView1?.findViewById<ImageView>(R.id.image_catergory_view)
        categoryView!!.text = getChild(groupPosition, childPosition).replace('_',' ').toUpperCase()
        categoryView.typeface = typefaceCategory
        val category = getChild(groupPosition, childPosition)

        when(category.toLowerCase()){




            "atm"->{
                imageView?.setImageResource(R.drawable.ic_outline_atm_24px)
            }
            "bank"->{
                imageView?.setImageResource(R.drawable.ic_outline_account_balance_24px)
            }
            "bar"->{
                imageView?.setImageResource(R.drawable.ic_glass_cocktail)
            }
            "bus_station"->{
                imageView?.setImageResource(R.drawable.ic_bus)
            }
            "bakery"->{
                imageView?.setImageResource(R.drawable.ic_outline_local_cafe_24px)
            }
            "car_wash"->{
                imageView?.setImageResource(R.drawable.ic_car_wash)
            }
            "gas_station"->{
                imageView?.setImageResource(R.drawable.ic_outline_local_gas_station_24px)
            }
            "hospital"->{
                imageView?.setImageResource(R.drawable.ic_outline_hotel_24px)
            }
            "pharmacy"->{
                imageView?.setImageResource(R.drawable.ic_pharmacy)
            }
            "restaurant"->{
                imageView?.setImageResource(R.drawable.ic_silverware_fork_knife)
            }
            "school"->{
                imageView?.setImageResource(R.drawable.ic_outline_school_24px)
            }
            "store"->{
                imageView?.setImageResource(R.drawable.ic_outline_shopping_cart_24px)
            }
            "taxi_station"->{
                imageView?.setImageResource(R.drawable.ic_taxi)
            }
            "train_station"->{
                imageView?.setImageResource(R.drawable.ic_train)
            }
            else->{
                imageView?.setImageResource(R.drawable.ic_outline_search_24px)

            }


        }




        if(isLastChild){
            convertView1 = View.inflate(context, R.layout.last_child_at_home_cardview,null)

            val categoryView1 = convertView1?.findViewById<TextView>(R.id.catergory_home_view_last_child)
            val imageView1 = convertView1?.findViewById<ImageView>(R.id.image_catergory_view_last_child)
            categoryView1!!.text = getChild(groupPosition, childPosition).replace('_',' ').toUpperCase()
            categoryView1.typeface = typefaceCategory

            when(category.toLowerCase()){




                "atm"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_atm_24px)
                }
                "bank"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_account_balance_24px)
                }
                "bar"->{
                    imageView1?.setImageResource(R.drawable.ic_glass_cocktail)
                }
                "bus_station"->{
                    imageView1?.setImageResource(R.drawable.ic_bus)
                }
                "bakery"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_local_cafe_24px)
                }
                "car_wash"->{
                    imageView1?.setImageResource(R.drawable.ic_car_wash)
                }
                "gas_station"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_local_gas_station_24px)
                }
                "hospital"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_hotel_24px)
                }
                "pharmacy"->{
                    imageView1?.setImageResource(R.drawable.ic_pharmacy)
                }
                "restaurant"->{
                    imageView1?.setImageResource(R.drawable.ic_silverware_fork_knife)
                }
                "school"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_school_24px)
                }
                "store"->{
                    imageView1?.setImageResource(R.drawable.ic_outline_shopping_cart_24px)
                }
                "taxi_station"->{
                    imageView1?.setImageResource(R.drawable.ic_taxi)
                }
                "train_station"->{
                    imageView1?.setImageResource(R.drawable.ic_train)
                }
                else->{
                    imageView1?.setImageResource(R.drawable.ic_outline_search_24px)

                }


            }
        }

        convertView1!!.setOnClickListener {

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                val intent = Intent(con, ViewCategory::class.java)
                val array = arrayListOf(parnetArrayList[groupPosition],childArray[groupPosition].elementAt(childPosition))
                intent.putStringArrayListExtra("data", array)
                val option = ActivityOptions.makeSceneTransitionAnimation(activity)
                ContextCompat.startActivity(context,intent,option.toBundle())

            }else{

                val intent = Intent(con, ViewCategory::class.java)
                val array = arrayListOf(parnetArrayList[groupPosition],childArray[groupPosition].elementAt(childPosition))
                intent.putStringArrayListExtra("data", array)
                ContextCompat.startActivity(context,intent,null)

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

    private fun toChangeLayoutByPosition(position: Int ,view: View?,inflater: LayoutInflater,groupPosition: Int,date : String,size:Int) :View? {
        var convertView = view

        convertView = inflater.inflate(R.layout.parent_card_for_else,null)
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



        return convertView
    }

    private fun toChangeLayoutByPositionWhenExpand(position: Int ,view: View?,inflater: LayoutInflater,groupPosition: Int,date : String,size:Int) :View? {
        var convertView = view

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