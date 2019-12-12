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
                        R.id.atm_menu ->{
                            itemView.catergory_chip_edit.chipText = "ATM"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "atm"
                        }
                        R.id.bank_menu ->{
                            itemView.catergory_chip_edit.chipText = "BANK"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "bank"
                        }
                        R.id.bar_menu ->{
                            itemView.catergory_chip_edit.chipText = "BAR"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "bar"
                        }
                        R.id.bus_station_menu ->{
                            itemView.catergory_chip_edit.chipText = "BUS STATION"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "bus_station"
                        }
                        R.id.cafe_menu ->{
                            itemView.catergory_chip_edit.chipText = "BAKERY"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "bakery"
                        }
                        R.id.car_wash_menu ->{
                            itemView.catergory_chip_edit.chipText = "CAR WASH"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "car_wash"
                        }
                        R.id.gas_station_menu ->{
                            itemView.catergory_chip_edit.chipText = "GAS STATION"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "gas_station"
                        }
                        R.id.hospital_menu ->{
                            itemView.catergory_chip_edit.chipText = "HOSPITAL"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "hospital"
                        }
                        R.id.pharmacy_menu ->{
                            itemView.catergory_chip_edit.chipText = "PHARMACY"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "pharmacy"
                        }
                        R.id.restaurant ->{
                            itemView.catergory_chip_edit.chipText = "RESTAURANT"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "restaurant"
                        }
                        R.id.school_menu ->{
                            itemView.catergory_chip_edit.chipText = "SCHOOL"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "school"
                        }
                        R.id.shop_menu ->{
                            itemView.catergory_chip_edit.chipText = "STORE"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "store"
                        }
                        R.id.taxi_menu ->{
                            itemView.catergory_chip_edit.chipText = "TAXI STATION"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "taxi_station"
                        }
                        R.id.train_menu ->{
                            itemView.catergory_chip_edit.chipText = "TRAIN STATION"
                            itemView.catergory_chip_edit.chipIcon =  pi.icon
                            GlobalCategory.returnGlobalCategory()[layoutPosition].shopCategory = "train_station"
                        }

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

