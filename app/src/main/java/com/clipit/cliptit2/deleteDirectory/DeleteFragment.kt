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
package com.clipit.cliptit2.deleteDirectory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.R
import com.clipit.cliptit2.con
import kotlinx.android.synthetic.main.fragment_delete__fragement.view.*


class DeleteFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_delete__fragement, container, false)
        val data = JsonDataBase(con).viewDelete()


        rootView.tool_bar_at_delete.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }

        val recycler=rootView.findViewById<RecyclerView>(R.id.deleteRecyclerView)
        recycler.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter= DeleteRecyclerViewAdapterClass(data.values, context!!, JsonDataBase(context!!))

        return rootView


    }

}
