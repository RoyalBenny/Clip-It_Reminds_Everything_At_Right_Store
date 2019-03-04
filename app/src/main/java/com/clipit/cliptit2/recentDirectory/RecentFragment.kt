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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.R
import com.clipit.cliptit2.con
import kotlinx.android.synthetic.main.fragment_recent_.view.*
import java.util.*


class RecentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_recent_, container, false)

        val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val db = JsonDataBase(con)
        val data = db.returnAutoBasedOnRecent()
        val dateSet: MutableSet<String> = java.util.HashSet()
        rootView.tool_bar_at_recent.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }


        data.forEach {

            dateSet.add(it.date!!)

        }

        val dateList: MutableList<String> = mutableListOf()

        dateList.addAll(dateSet)
        dateList.sortWith(kotlin.Comparator { o1, o2 ->
            dateFormat.parse(o2).compareTo(dateFormat.parse(o1))
        })


        val child:MutableList<MutableSet<String>> = mutableListOf()


        dateList.forEach {

            val hashSet:MutableSet<String> = HashSet()
            data.forEach { pi ->
                if (it == pi.date) {
                    hashSet.add(pi.shopCategory!!)

                }


            }
            child.add(hashSet)


        }




        val expListView=rootView.findViewById<ExpandableListView>(R.id.expandable_list_view_at_recent)
        val listAdapter  = ExpandableListViewAdapter3(con, expListView, dateList, child,activity!!)
        expListView.setAdapter(listAdapter)
        expListView.addFooterView(View.inflate(con, R.layout.footer_layout,null ))
        expListView.addHeaderView(View.inflate(con, R.layout.header_layout,null))





        return rootView
    }


}
