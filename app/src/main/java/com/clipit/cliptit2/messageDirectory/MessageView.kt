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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import com.clipit.cliptit2.firebaseDirectory.MessageClass
import com.clipit.cliptit2.firebaseDirectory.MessageGlobalClass
import com.clipit.cliptit2.loginDirectory.LoginActivity
import com.clipit.cliptit2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_message_view.view.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
var messageAdapter : MessageRecyclerClass? = null



class MessageView : Fragment() {


    private lateinit var rootView : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_message_view, container, false)
        val fBData = FirebaseDatabase.getInstance().getReference("messages")
        val fbAuth = FirebaseAuth.getInstance()
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()


        rootView.tool_bar_at_message_view.setNavigationOnClickListener {
            activity!!.onBackPressed()

        }


        if (mAuth.currentUser==null) {
            val intent = Intent(context, LoginActivity::class.java)
            context!!.startActivity(intent)
        }else {

            val userPhone = fbAuth.currentUser!!.phoneNumber
            val messageGlobalClass = MessageGlobalClass.messageArray
            messageGlobalClass.clear()
            val dateFormat = SimpleDateFormat("dd MMM yyyy | HH:mm", Locale.US)
            fBData.child(userPhone!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        messageGlobalClass.add(it.getValue(MessageClass::class.java)!!)

                    }

                    messageGlobalClass.sortByDescending {
                        return@sortByDescending dateFormat.parse(it.timeStamp)

                    }


                    try {
                        adapterAttach(messageGlobalClass)

                    }catch (e:Exception){
                        e.printStackTrace()
                    }


                }
            }
            )
        }
        return rootView

    }




    fun adapterAttach (adapter: MutableList<MessageClass>) {

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.messageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)


        messageAdapter = MessageRecyclerClass(adapter, context!!)
        recyclerView.adapter = messageAdapter

    }

}

