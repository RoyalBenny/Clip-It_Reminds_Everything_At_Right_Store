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

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPageAdapter(manger: FragmentManager) : FragmentPagerAdapter(manger) {

    private val mFragment : MutableList<Fragment> = mutableListOf()
    private val mFragmentTitle : MutableList<String> = mutableListOf()

    override fun getItem(p0: Int): Fragment {

        return mFragment[p0]


    }

    fun addFragment(fragment: Fragment , title:String){
        mFragment.add(fragment)
        mFragmentTitle.add(title)

    }

    override fun getCount(): Int {
        return mFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitle[position]
    }
}