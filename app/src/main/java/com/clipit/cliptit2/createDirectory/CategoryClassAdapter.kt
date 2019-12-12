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

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.clipit.cliptit2.*
import com.clipit.cliptit2.globalVariablesDirectory.GlobalCategory
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import kotlinx.android.synthetic.main.recycler_new_card_create_activity.view.*

var itemName2 : ArrayList<String> = ArrayList()

class CategoryClassAdapter(val list:ArrayList<ShopClass>, var placeAutocompleteAdapter: PlaceAutocompleteAdapter) : RecyclerView.Adapter<CategoryClassAdapter.ViewHolder>() {

    private var k : CharSequence? = "ss"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val view = v.inflate(R.layout.recycler_new_card_create_activity, parent, false)


        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         init {

            itemView.catergory_chip_edit.setOnClickListener {
                val popupMenu = PopupMenu(con,it)
                popupMenu.inflate(R.menu.category_items)


                try {


                    val fieldPopupMenu = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldPopupMenu.isAccessible = true
                    val mPopupMenu = fieldPopupMenu.get(popupMenu)
                    mPopupMenu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java).invoke(mPopupMenu,true)
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    popupMenu.show()
                }

                popupMenu.setOnMenuItemClickListener { pi->
                    when(pi.itemId){


                    }


                     true
                }

          }

            itemView.place_auto_complete_text_create.setAdapter(placeAutocompleteAdapter)
            itemView.place_auto_complete_text_create.addTextChangedListener(object  : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    val text = s!!.split(",")

                    GlobalCategory.returnGlobalCategory()[layoutPosition].shopLocation = text[0]
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    k= s

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    k=s
                }

            })

      itemName2.add("")
            itemView.recycler_edit_text.addTextChangedListener(object  : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    itemName2[layoutPosition] = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    k = s

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    k = s
                }

            })

        }
    }

}

