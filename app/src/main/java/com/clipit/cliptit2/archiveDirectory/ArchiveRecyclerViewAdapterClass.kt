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

package com.clipit.cliptit2.archiveDirectory

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.clipit.cliptit2.*
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.deleteDirectory.isRestored
import com.clipit.cliptit2.globalVariablesDirectory.ItemClass
import com.clipit.cliptit2.loginDirectory.LoginActivity
import com.clipit.cliptit2.messageDirectory.MessagingActivity
import com.google.firebase.auth.FirebaseAuth


class ArchiveRecyclerViewAdapterClass(val list: MutableCollection<MutableList<ItemClass>>, val context: Context, val db: JsonDataBase) : RecyclerView.Adapter<ArchiveRecyclerViewAdapterClass.ViewHolder>(){

    private val newtypefaceCategory = Typeface.createFromAsset(context.assets, "fonts/Armata-Regular.ttf")
    private val newtypefaceItems = Typeface.createFromAsset(context.assets, "fonts/Karma-Regular.ttf")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val view = v.inflate(R.layout.delete_card_view, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemTextView.text = ""
        list.elementAt(position).forEach {
            holder.itemTextView.append(it.itemName+"\n")
        }


       }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView = itemView.findViewById<TextView>(R.id.deleteItems)!!
        val categoryTextView = itemView.findViewById<TextView>(R.id.deleteCategory)!!
        private val card = itemView.findViewById<CardView>(R.id.deleteCArdView)!!
        val imageView = itemView.findViewById<ImageView>(R.id.image_view_at_delete_card)!!
        init {


        }


    }

    private fun imageFun(name: String, holder: ViewHolder) {
        when (name.toLowerCase()) {
            "atm" -> {
                holder.imageView.setImageResource(R.drawable.atm)
            }
            "bank" -> {
                holder.imageView.setImageResource(R.drawable.bank)
            }
            "bar" -> {
                holder.imageView.setImageResource(R.drawable.bar)
            }
            "bus_station" -> {
                holder.imageView.setImageResource(R.drawable.bus_station)
            }
            "bakery" -> {
                holder.imageView.setImageResource(R.drawable.cafe_new)
            }
            "car_wash" -> {
                holder.imageView.setImageResource(R.drawable.car_wash)
            }
            "gas_station" -> {
                holder.imageView.setImageResource(R.drawable.gas_station)
            }
            "hospital" -> {
                holder.imageView.setImageResource(R.drawable.hospital)
            }
            "pharmacy" -> {
                holder.imageView.setImageResource(R.drawable.phramavy)
            }
            "restaurant" -> {
                holder.imageView.setImageResource(R.drawable.restaurant)
            }
            "school" -> {
                holder.imageView.setImageResource(R.drawable.school)
            }
            "store" -> {
                holder.imageView.setImageResource(R.drawable.shops)
            }
            "taxi_station" -> {
                holder.imageView.setImageResource(R.drawable.car_wash)
            }
            "train_station" -> {
                holder.imageView.setImageResource(R.drawable.train_station)
            }
            else -> {
                holder.imageView.setImageResource(R.drawable.cafe)
            }
        }


    }



}

