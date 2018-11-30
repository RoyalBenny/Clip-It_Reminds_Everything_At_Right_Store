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
package com.clipit.cliptit2.tabbedViewDirectory

import android.support.design.widget.TabLayout

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clipit.cliptit2.R

val nearShopMap  = mutableMapOf<String,Int>()
open class TabbedViewBottomSheet : Fragment() {

    private lateinit var tableLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var adapter : ViewPageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_tabbed_view_bottom_sheet,container,false)
        retainInstance = true
        viewPager = rootView.findViewById(R.id.viewPager) as ViewPager
        setupViewPager(viewPager)
        tableLayout = rootView.findViewById(R.id.tabLayout) as TabLayout
        tableLayout.setupWithViewPager(viewPager)
        tableLayout.getTabAt(0)!!.setIcon(R.drawable.selector_from_icons_in_tab_view)
        tableLayout.getTabAt(1)!!.setIcon(R.drawable.selector_for)


      return rootView
    }





    private fun setupViewPager(viewPager: ViewPager) {
        try {
            adapter = ViewPageAdapter(this.childFragmentManager)
            adapter.addFragment(Items(),"Items")
            adapter.addFragment(Maps(),"Maps")
            viewPager.adapter = adapter
        }catch (e:Exception){e.printStackTrace()}

    }









}

