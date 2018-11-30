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
package com.clipit.cliptit2.messageDirectory

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.clipit.cliptit2.firebaseDirectory.MessageClass
import com.clipit.cliptit2.R


class MessageRecyclerClass(val list:MutableList<MessageClass>, val context: Context) : RecyclerView.Adapter<MessageRecyclerClass.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
        val view = v.inflate(R.layout.message_view_card, parent, false)
        return ViewHolder(view)


    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.senderTextView.text = list[position].senderName

        if(list[position].seen){
            holder.seenTextView.setImageResource(R.drawable.ic_outline_done_all_24px)
        }
        else{
            holder.seenTextView.setImageResource(R.drawable.ic_outline_done_all_24px_2)
        }
        holder.messageTextView.text = list[position].message
        holder.timeTextView.text = list[position].timeStamp
        holder.letterView.text=list[position].senderName[0].toString()
        changecolor(list[position].senderName[0],holder)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderTextView = itemView.findViewById<TextView>(R.id.senderNameView)!!
        val seenTextView = itemView.findViewById<ImageView>(R.id.seenView)!!
        val messageTextView = itemView.findViewById<TextView>(R.id.messageView)!!
        val timeTextView = itemView.findViewById<TextView>(R.id.timeView)!!
        val letterView = itemView.findViewById<TextView>(R.id.letter_view_in_meesage_view)!!


        init {

            itemView.setOnClickListener {
                val intent = Intent(context, MessageViewDetail::class.java)
                intent.putExtra("position",layoutPosition)
                context.startActivity(intent)

            }


        }

    }

    private fun changecolor(char : Char,holder2: ViewHolder) {

        when(char.toUpperCase()){
            'A'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue_ink_))
            'B'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.shrine_colour_primay_pink))
            'C'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_bar_color_home))
            'D'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow_colour))
            'E'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.borrown_color))
            'F'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_blue_100))
            'G'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_color_50))
            'H'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_error_50))
            'I'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPurple500))
            'J'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGrey))
            'K'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorLightBlue300))
            'L'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGreen400))
            'M'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.materialViolet))
            'N'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.materialGrey))
            'O'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_blue_strong_200))
            'P'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_rose_strong_100))
            'Q'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_rose_strong_200))
            'R'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_strong_blue_50))
            'S'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_strong_violet_50))
            'T'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
            'U'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_100))
            'V'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.new_material_grey_50))
            'W'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGreen800))
            'X'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material))
            'Y'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_50))
            'Z'->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_color_30))

            else->holder2.letterView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
        }

    }

}

