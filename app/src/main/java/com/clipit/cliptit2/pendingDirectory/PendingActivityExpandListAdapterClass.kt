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

import android.content.ContentValues
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.support.design.chip.Chip
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.databaseDirectory.autoAllItemsBrought
import com.clipit.cliptit2.databaseDirectory.autoIncrementNumber
import com.clipit.cliptit2.databaseDirectory.autoIncrementTable
import com.clipit.cliptit2.globalVariablesDirectory.ItemClass
import com.clipit.cliptit2.R


class PendingActivityExpandListAdapterClass(var context: Context, private var expandableListView: ExpandableListView, private var parnetArrayList: MutableList<String>, private var childArray: ArrayList<ArrayList<ItemClass>>) : BaseExpandableListAdapter() {
    private val typefaceCategory = Typeface.createFromAsset(context.assets,"fonts/NotoSerif-Italic.ttf")
    private val typefaceItem = Typeface.createFromAsset(context.assets,"fonts/NotoSerif-Regular.ttf")
    private val db = JsonDataBase(context)

    override fun getGroup(groupPosition: Int): String {
        return parnetArrayList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView1 = convertView

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView1 = inflater.inflate(R.layout.parent_for_pending_activity,null)

        val categoryView = convertView1?.findViewById<Chip>(R.id.category_chip_for_expandable_listview_in_at_pendin_intent)!!
        categoryView.typeface = typefaceCategory
        categoryView.text = parnetArrayList[groupPosition].toUpperCase()
        expandableListView.expandGroup(groupPosition)

        return convertView1
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childArray[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): ItemClass {

        return childArray[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView1=convertView

        val inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView1=inflater.inflate(R.layout.child_item_card,null)

        val itemViewExpand = convertView1?.findViewById<TextView>(R.id.item_textView_for_category_view)
        itemViewExpand!!.text = getChild(groupPosition,childPosition).itemName
        itemViewExpand.typeface = typefaceItem

        val checkedBox = convertView1?.findViewById<CheckBox>(R.id.checkBoxItem)
        checkedBox?.isChecked = childArray[groupPosition][childPosition].checked == 1
        if(checkedBox?.isChecked!!){
            val strike = StrikethroughSpan()
            val text = SpannableString(childArray[groupPosition][childPosition].itemName)
            text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemViewExpand.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
            itemViewExpand.typeface = typefaceItem
            itemViewExpand.text = text
            checkedBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))

        }




        checkedBox.setOnClickListener {
            if (checkedBox.isChecked ) {
                val strike = StrikethroughSpan()
                val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                itemViewExpand.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                itemViewExpand.typeface = typefaceItem
                itemViewExpand.text = text
                childArray[groupPosition][childPosition].checked = 1
                checkedBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                val data = childArray[groupPosition][childPosition]
                db.updateItemTableWhenChecked(data.id!!,data.itemName!!,1)
                if(childArray[groupPosition].filter {pi-> pi.checked==1 }.size == childArray[groupPosition].size){
                    val cv = ContentValues()
                    cv.put(autoAllItemsBrought,1)
                    db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(childArray[groupPosition][childPosition].id!!.toString()))

                }

            } else {

                itemViewExpand.typeface = typefaceItem
                itemViewExpand.text = childArray[groupPosition][childPosition].itemName
                itemViewExpand.setTextColor(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                checkedBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                val data = childArray[groupPosition][childPosition]
                childArray[groupPosition][childPosition].checked = 0
                db.updateItemTableWhenChecked(data.id!!,data.itemName!!,0)
                val cv = ContentValues()
                cv.put(autoAllItemsBrought,0)
                db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(childArray[groupPosition][childPosition].id!!.toString()))

            }
        }

        if (isLastChild){
            convertView1 = inflater.inflate(R.layout.last_child_at_pending_activity, null)
            val itemView = convertView1?.findViewById<TextView>(R.id.item_textView_for_category_view_main_last)
            itemView!!.text = getChild(groupPosition, childPosition).itemName
            itemView.typeface = typefaceItem
            val checkBox = convertView1?.findViewById<CheckBox>(R.id.checkBoxItem_view_category_main_last)
            checkBox?.isChecked = childArray[groupPosition][childPosition].checked == 1
            if(checkBox!!.isChecked||childArray[groupPosition][childPosition].checked==1){
                val strike = StrikethroughSpan()
                val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                itemView.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                itemView.typeface = typefaceItem
                itemView.text = text
                checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
            }


            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    val strike = StrikethroughSpan()
                    val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                    text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    itemView.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                    itemView.typeface = typefaceItem
                    itemView.text = text
                    childArray[groupPosition][childPosition].checked = 1
                    val data = childArray[groupPosition][childPosition]
                    checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                    db.updateItemTableWhenChecked(data.id!!,data.itemName!!,1)
                    if(childArray[groupPosition].filter {pi-> pi.checked==1 }.size == childArray[groupPosition].size){
                        val cv = ContentValues()
                        cv.put(autoAllItemsBrought,1)
                        db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(childArray[groupPosition][childPosition].id!!.toString()))

                    }

                } else {

                    itemView.typeface = typefaceItem
                    itemView.text = childArray[groupPosition][childPosition].itemName
                    itemView.setTextColor(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                    checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                    val data = childArray[groupPosition][childPosition]
                    db.updateItemTableWhenChecked(data.id!!,data.itemName!!,0)
                    childArray[groupPosition][childPosition].checked = 0
                    val cv = ContentValues()
                    cv.put(autoAllItemsBrought,0)
                    db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(childArray[groupPosition][childPosition].id!!.toString()))


                }
            }
        }




        return convertView1
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return  childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return parnetArrayList.size
    }
}