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

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.clipit.cliptit2.globalVariablesDirectory.GlobalCategory
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.clipit.cliptit2.createDirectory.PlaceAutocompleteAdapter
import com.clipit.cliptit2.R

var itemName3 : ArrayList<String> = ArrayList()

class NewItemCreateAdapter(val list:ArrayList<ShopClass>, var placeAutocompleteAdapter: PlaceAutocompleteAdapter, var category:String) : RecyclerView.Adapter<NewItemCreateAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val view = v.inflate(R.layout.create_new_card_for_recyelerview_for_catgeroy_view_main, parent, false)


        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemEditText = itemView.findViewById<EditText>(R.id.recycler_edit_text_in_category_view)
        private val placeAuto = itemView.findViewById<AutoCompleteTextView>(R.id.place_auto_complete_text_create_for_category_view)

        init {
            var k:CharSequence=""
            val global = GlobalCategory.CategoryGlobal

            placeAuto.setAdapter(placeAutocompleteAdapter)
            placeAuto.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val text = s!!.split(",")
                    global.arrayGlobalCategory[layoutPosition].shopCategory=category
                    global.returnGlobalCategory()[layoutPosition].shopLocation = text[0]
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    k = s!!

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    k = s!!
                }

            })

            itemName3.add("")
            itemEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    itemName3[layoutPosition] = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    k = s!!

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    k = s!!
                }

            })


        }


    }

}

