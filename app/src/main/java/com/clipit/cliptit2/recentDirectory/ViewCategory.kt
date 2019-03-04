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

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.chip.Chip
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.*
import android.widget.*
import com.clipit.cliptit2.backgroundDirectory.JsonDownloadAsync
import com.clipit.cliptit2.databaseDirectory.*
import com.clipit.cliptit2.deleteDirectory.isRestored
import com.clipit.cliptit2.globalVariablesDirectory.GlobalCategory
import com.clipit.cliptit2.globalVariablesDirectory.ItemClass
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import com.clipit.cliptit2.homeDirectory.BottomSheetaDialogFragment
import com.clipit.cliptit2.homeDirectory.itemDetailArray
import com.clipit.cliptit2.homeDirectory.itemName3
import com.clipit.cliptit2.homeDirectory.map
import com.clipit.cliptit2.loginDirectory.LoginActivity
import com.clipit.cliptit2.messageDirectory.MessagingActivity
import com.clipit.cliptit2.createDirectory.PlaceAutocompleteAdapter
import com.clipit.cliptit2.R
import com.clipit.cliptit2.tabbedViewDirectory.ViewPageAdapter
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_view_fragment_for_category_click.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.work.*
import androidx.work.Data
import com.clipit.cliptit2.backgroundDirectory.JsonDataBaseBackGroundProcess


class ViewCategory : AppCompatActivity(){
    private var select = 0
    private var date :String =""
    private var category : String? =null
    private var expandList: ExpandableItemListAdapter? = null
    private lateinit var placeAutocompleteAdapter : PlaceAutocompleteAdapter
    private lateinit var mGeoDataClient : GeoDataClient
    private lateinit var latLngBounds : LatLngBounds
    private val data = GlobalCategory.CategoryGlobal
    private var i =0
    private val db = JsonDataBase(this)
    private var resend = true







    private val mActionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.menu_long_click_on_card, menu)
            return true
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.send_card_on_long_click -> {
                    val mAuth = FirebaseAuth.getInstance()
                    if (mAuth.currentUser==null) {
                        val intent = Intent(this@ViewCategory, LoginActivity::class.java)
                        this@ViewCategory.startActivity(intent)
                        return false
                    }
                    else {
                        if (select < 1) {
                            return false
                        }
                        val items = java.util.ArrayList<String>()
                        map.values.forEach {
                            items.add(it.itemName!!)
                        }

                        val intent = Intent(this@ViewCategory, MessagingActivity::class.java)
                        intent.putExtra("category", category)
                        intent.putStringArrayListExtra("items", items)
                        startActivity(intent)
                        return true
                    }
                }
                R.id.share_card_on_long_click ->{
                    var text = ""
                    map.values.forEach {
                        text+=it.itemName+"\n"

                    }
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,text)
                        type = "text/plain"
                    }
                    startActivity(intent)
                    return true
                }

                R.id.delete_card_on_long_click ->{
                    deleteItems()
                    return true
                }

                R.id.archive_on_long_click ->{
                    archiveItems()
                    return true
                }

                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            select =0
            map.clear()
            tool_bar_at_category_view2.visibility=View.VISIBLE
            expandList!!.notifyDataSetChanged()
            return
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_view_fragment_for_category_click)
        setSupportActionBar(tool_bar_at_category_view2)
        val typefaceItem = Typeface.createFromAsset(applicationContext.assets, "fonts/NotoSerif-Regular.ttf")



        if (android.os.Build.VERSION.SDK_INT >= 23) {
            val windows: Window = this.window
            windows.statusBarColor = this.getColor(R.color.material_new_brown_strong_10_transparent)
        }

        tool_bar_at_category_view2.setNavigationOnClickListener {
            onBackPressed()
        }


        val intent = intent
        val idIntent = intent.getStringArrayListExtra("data")
        date = idIntent[0]
        category = idIntent[1]


        val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val calender = Calendar.getInstance()
        latLngBounds = LatLngBounds(
                LatLng((-40).toDouble(), (-168).toDouble()), LatLng(71.toDouble(), 136.toDouble())
        )
        mGeoDataClient = Places.getGeoDataClient(this, null)
        placeAutocompleteAdapter = PlaceAutocompleteAdapter(this, mGeoDataClient, latLngBounds, null)

        collaspe_toolbar_for_category_view.title = "CLIP-IT"
        tool_bar_at_category_view2.title = ""




        category_view_text_in_ViewCategory.text = category!!.replace('_',' ').toUpperCase()
        category_view_text_in_ViewCategory.typeface = typefaceItem

        val imageView = findViewById<ImageView>(R.id.image_view_in_collapse_toolbar_view_at_category_view)

        when (category!!.toLowerCase()) {

            "atm" -> {
                imageView?.setImageResource(R.drawable.atm)
            }
            "bank" -> {
                imageView?.setImageResource(R.drawable.bank)
            }
            "bar" -> {
                imageView?.setImageResource(R.drawable.bar)
            }
            "bus_station" -> {
                imageView?.setImageResource(R.drawable.bus_station)
            }
            "bakery" -> {
                imageView?.setImageResource(R.drawable.cafe_new)
            }
            "car_wash" -> {
                imageView?.setImageResource(R.drawable.car_wash)
            }
            "gas_station" -> {
                imageView?.setImageResource(R.drawable.gas_station)
            }
            "hospital" -> {
                imageView?.setImageResource(R.drawable.hospital)
            }
            "pharmacy" -> {
                imageView?.setImageResource(R.drawable.phramavy)
            }
            "restaurant" -> {
                imageView?.setImageResource(R.drawable.restaurant)
            }
            "school" -> {
                imageView?.setImageResource(R.drawable.school)
            }
            "store" -> {
                imageView?.setImageResource(R.drawable.shops)
            }
            "taxi_station" -> {
                imageView?.setImageResource(R.drawable.car_wash)
            }
            "train_station" -> {
                imageView?.setImageResource(R.drawable.train_station)
            }
            else -> {
                imageView?.setImageResource(R.drawable.cafe)

            }

        }

        val imageIconView = findViewById<ImageView>(R.id.icon_view_in_category_view)
        when (category!!.toLowerCase()) {
            "atm" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_atm_24px)
            }
            "bank" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_account_balance_24px)
            }
            "bar" -> {
                imageIconView?.setImageResource(R.drawable.ic_glass_cocktail)
            }
            "bus_station" -> {
                imageIconView?.setImageResource(R.drawable.ic_bus)
            }
            "bakery" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_local_cafe_24px)
            }
            "car_wash" -> {
                imageIconView?.setImageResource(R.drawable.ic_car_wash)
            }
            "gas_station" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_local_gas_station_24px)
            }
            "hospital" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_hotel_24px)
            }
            "pharmacy" -> {
                imageIconView?.setImageResource(R.drawable.ic_pharmacy)
            }
            "restaurant" -> {
                imageIconView?.setImageResource(R.drawable.ic_silverware_fork_knife)
            }
            "school" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_school_24px)
            }
            "store" -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_shopping_cart_24px)
            }
            "taxi_station" -> {
                imageIconView?.setImageResource(R.drawable.ic_taxi)
            }
            "train_station" -> {
                imageIconView?.setImageResource(R.drawable.ic_train)
            }
            else -> {
                imageIconView?.setImageResource(R.drawable.ic_outline_search_24px)

            }

        }

        val db = JsonDataBase(this)
        val idArray = db.returnIdBasedOnDateAndCate(date,category!!).filter {
            it.delete ==0  && it.archive==0
        }


        itemDetailArray.clear()
        idArray.forEach {
            val data = db.returnItemBasedOnId(it.id!!)
            itemDetailArray.add(data)

        }

        var size = 0
        itemDetailArray.forEach {
            size += it.size
        }


        val params = expnadable_listview_for_item_view_of_category_view.layoutParams
        expnadable_listview_for_item_view_of_category_view.measure(0, 0)
        params.height = size * 200
        expnadable_listview_for_item_view_of_category_view.layoutParams = params
        expnadable_listview_for_item_view_of_category_view.requestLayout()


        val inflater = layoutInflater


        expandList = ExpandableItemListAdapter(this, expnadable_listview_for_item_view_of_category_view, idArray, itemDetailArray, inflater, supportFragmentManager, category!!)
        expnadable_listview_for_item_view_of_category_view.setAdapter(expandList)


        val adapterRec = ArrayList<ShopClass>()
        adapterRec.add(ShopClass())
        data.arrayGlobalCategory = adapterRec
        floating_button_to_create_new_item_in_category_view.setOnClickListener {pi->
            val dpt = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                date = dateFormat.format(selectedDate.time)

                val cv = ContentValues()
                cv.put(autoDateColumn,date)
                var j =-1
                idArray.forEach {
                    j =  db.writableDatabase.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(it.id!!.toString()))
                    isRestored = 1

                    if(j!=-1)Snackbar.make(pi,"Items restored to $date",Snackbar.LENGTH_SHORT).show()

                }

            }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH))
            dpt.datePicker.minDate = System.currentTimeMillis()-1000
            dpt.show()




        }
    }




    inner class ExpandableItemListAdapter(var context: Context,private var expandableListView: ExpandableListView,private var parnetArrayList: List<ShopClass>,private var childArray: ArrayList<ArrayList<ItemClass>>,private var inflater: LayoutInflater,private var manger: FragmentManager,private val category: String) : BaseExpandableListAdapter(){

        private val typefaceChip = Typeface.createFromAsset(context.assets,"fonts/Slabo27px-Regular.ttf")
        private val typefaceItem = Typeface.createFromAsset(context.assets,"fonts/NotoSerif-Regular.ttf")
        private lateinit var adapter : ViewPageAdapter
        private val db = JsonDataBase(this@ViewCategory)

        override fun getGroup(groupPosition: Int): ShopClass {
            return parnetArrayList[groupPosition]
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return true
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
            var convertView1 = convertView

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView1 = inflater.inflate(R.layout.parent_place_expand_for_category_view_expandview,null)

            val restore = convertView1.findViewById<ImageButton>(R.id.restore_button_at_view_category)
            val categoryView = convertView1?.findViewById<Chip>(R.id.location_chip_for_expandable_listview_in_category_view)

            expandableListView.expandGroup(groupPosition)
            categoryView?.chipText = getGroup(groupPosition).shopLocation
            categoryView?.typeface = typefaceChip
            val array = db.returnJsonBasedOnId(parnetArrayList[groupPosition].id!!)
            if(array.isEmpty()){
                restore.setOnClickListener {
                    if(resend){
                        timer()


                        val workData:Data  = mapOf("location" to parnetArrayList[groupPosition].shopLocation,"category" to parnetArrayList[groupPosition].shopCategory ,"date" to parnetArrayList[groupPosition].date,"id" to parnetArrayList[groupPosition].id.toString()).toWorkData()
                        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                        val background = OneTimeWorkRequest.Builder(JsonDataBaseBackGroundProcess::class.java).setConstraints(constraints).setInputData(workData).build()
                        WorkManager.getInstance().enqueue(background)
                        Snackbar.make(it,"Refreshing",Snackbar.LENGTH_SHORT).show()
                        restore.visibility = View.GONE

                    }
                }
            }else{
                restore.visibility = View.GONE
            }


            categoryView?.setOnClickListener {

                if (array.isNotEmpty()) {

                    val bottomSheet = BottomSheetaDialogFragment()
                    val text = Bundle()
                    text.putString("place", parnetArrayList[groupPosition].shopLocation)
                    text.putInt("id", parnetArrayList[groupPosition].id!!)
                    bottomSheet.arguments = text
                    manger.beginTransaction().add(R.id.layout_to_use_as_map,bottomSheet).addToBackStack(null).commit()
                    i=1

                }else{
                    Toast.makeText(context,"No data",Toast.LENGTH_SHORT).show()
                }
            }

            return convertView1

        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return childArray[groupPosition].size
        }

        override fun getChild(groupPosition: Int, childPosition: Int): ItemClass {

            return childArray[groupPosition][childPosition]
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
            var convertView1=convertView
            val inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            convertView1=inflater.inflate(R.layout.child_item_card,null)

            val itemViewExpand = convertView1?.findViewById<TextView>(R.id.item_textView_for_category_view)
            itemViewExpand!!.text = getChild(groupPosition,childPosition).itemName
            itemViewExpand.typeface = typefaceItem


            val checkedBox = convertView1?.findViewById<CheckBox>(R.id.checkBoxItem)
            checkedBox?.isChecked = childArray[groupPosition][childPosition].checked==1

            if(checkedBox?.isChecked!!){
                val strike = StrikethroughSpan()
                val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                itemViewExpand.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                itemViewExpand.typeface = typefaceItem
                itemViewExpand.text = text
                checkedBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))

            }



            if(isLastChild) {
                convertView1 = inflater.inflate(R.layout.last_child_for_itemview_in_category_view, null)
                val itemView = convertView1?.findViewById<TextView>(R.id.item_textView_for_category_view)
                itemView!!.text = getChild(groupPosition, childPosition).itemName
                itemView.typeface = typefaceItem
                val checkBox = convertView1?.findViewById<CheckBox>(R.id.checkBoxItem)
                checkBox?.isChecked = childArray[groupPosition][childPosition].checked == 1
                if(checkBox!!.isChecked){
                    val strike = StrikethroughSpan()
                    val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                    text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    itemView.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                    itemView.typeface = typefaceItem
                    itemView.text = text
                    checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))

                }


                checkBox.setOnClickListener {
                    if (checkBox.isChecked) {
                        val strike = StrikethroughSpan()
                        val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                        text.setSpan(strike, 0, childArray[groupPosition][childPosition].itemName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        itemView.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                        itemView.typeface = typefaceItem
                        itemView.text = text
                        checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                        itemDetailArray[groupPosition][childPosition].checked=1
                        val data = itemDetailArray[groupPosition][childPosition]
                        db.updateItemTableWhenChecked(data.id!!,data.itemName!!,1)
                        if(itemDetailArray[groupPosition].filter { pi-> pi.checked==1 }.size == itemDetailArray[groupPosition].size){
                            val cv = ContentValues()
                            cv.put(autoAllItemsBrought,1)
                            db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(itemDetailArray[groupPosition][childPosition].id!!.toString()))

                        }

                    } else {

                        itemView.typeface = typefaceItem
                        itemView.text = childArray[groupPosition][childPosition].itemName
                        itemView.setTextColor(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                        checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                        itemDetailArray[groupPosition][childPosition].checked = 0
                        val data = itemDetailArray[groupPosition][childPosition]
                        db.updateItemTableWhenChecked(data.id!!, data.itemName!!, 0)
                        val cv = ContentValues()
                        cv.put(autoAllItemsBrought,0)
                        db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(itemDetailArray[groupPosition][childPosition].id!!.toString()))


                    }
                }
            }

            checkedBox.setOnClickListener {

                itemViewExpand.typeface = typefaceItem
                if(checkedBox.isChecked){
                    val strike = StrikethroughSpan()
                    val text = SpannableString(childArray[groupPosition][childPosition].itemName)
                    text.setSpan(strike,0,childArray[groupPosition][childPosition].itemName!!.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    itemViewExpand.setTextColor(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                    itemViewExpand.typeface = typefaceItem
                    itemViewExpand.text = text
                    itemDetailArray[groupPosition][childPosition].checked=1
                    checkedBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_grey_strong_20))
                    val data = itemDetailArray[groupPosition][childPosition]
                    db.updateItemTableWhenChecked(data.id!!,data.itemName!!,1)
                    if(itemDetailArray[groupPosition].filter { pi-> pi.checked==1 }.size == itemDetailArray[groupPosition].size){
                        val cv = ContentValues()
                        cv.put(autoAllItemsBrought,1)
                        db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(itemDetailArray[groupPosition][childPosition].id!!.toString()))

                    }
                }else{


                    itemViewExpand.typeface = typefaceItem
                    itemViewExpand.text = childArray[groupPosition][childPosition].itemName
                    itemViewExpand.setTextColor(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                    checkedBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_new_brown_strong_50))
                    itemDetailArray[groupPosition][childPosition].checked=0
                    val data = itemDetailArray[groupPosition][childPosition]
                    db.updateItemTableWhenChecked(data.id!!,data.itemName!!,0)
                    val cv = ContentValues()
                    cv.put(autoAllItemsBrought,0)
                    db.writeDataBase()!!.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(itemDetailArray[groupPosition][childPosition].id!!.toString()))


                }
            }



            val card = convertView1.findViewById<CardView>(R.id.itemCardEdit)

            convertView1!!.setOnClickListener {

                if (select > 0) {

                    if (!it.isSelected) {
                        it.isSelected = true
                        map[Pair(groupPosition, childPosition)] = itemDetailArray[groupPosition][childPosition]

                        card.setCardBackgroundColor(convertView1.resources.getColor(R.color.material_new_grey_strong_20))
                        select++
                    } else if (select > 1) {
                        map.remove(Pair(groupPosition, childPosition))
                        card.setCardBackgroundColor(convertView1.resources.getColor(R.color.white))
                        it.isSelected = false
                        select--
                    }


                    return@setOnClickListener
                }


            }



            convertView1.setOnLongClickListener {


                this@ViewCategory.startActionMode(mActionModeCallback)

                tool_bar_at_category_view2.visibility=View.GONE
                if(!it.isSelected){
                    it.isSelected=true
                    map[Pair(groupPosition,childPosition)] = itemDetailArray[groupPosition][childPosition]
                    card!!.setCardBackgroundColor(convertView1.resources.getColor(R.color.material_new_grey_strong_20))
                    select++
                }
                return@setOnLongClickListener false

            }





            return convertView1
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return  childPosition.toLong()
        }

        override fun getGroupCount(): Int {
            return parnetArrayList.size
        }

    }


    override fun onBackPressed() {
        if(select>0) {
            map.clear()
            expandList!!.notifyDataSetChanged()
            select=0
            return
        }

        if(data.returnGlobalCategory().isNotEmpty() && itemName3.isNotEmpty()){
            val async = JsonDownloadAsync(date, this, 2)
            async.execute()
        }

        if(i==1){
            i=0
        }else {
            itemDetailArray.clear()
        }

        db.viewDelete()
        super.onBackPressed()
    }

    private fun deleteItems(){
        try {
            db.insertDeleteItem(map.values)
            val k= map.keys.sortedWith(
                    kotlin.Comparator { o1, o2 ->
                        (o2.first*100+o2.second).compareTo(o1.first*100+o1.second)
                    }
            )




            k.forEach {
                itemDetailArray[it.first].removeAt(it.second)
                if (itemDetailArray[it.first].isEmpty()) {
                    val dataBase = db.writeDataBase()
                    val cv = ContentValues()
                    cv.put(autoAllItemsDeleted, 1)
                    dataBase!!.update(autoIncrementTable, cv, "$autoIncrementNumber=?", arrayOf(map[it]!!.id!!.toString()))
                }
            }
            expandList!!.notifyDataSetChanged()
            map.clear()
        }catch (e:Exception){
            e.printStackTrace()
        }







    }

    private fun archiveItems(){
        try {
            db.insertArchive(map.values)
            val k= map.keys.sortedWith(
                    kotlin.Comparator { o1, o2 ->
                        (o2.first*100+o2.second).compareTo(o1.first*100+o1.second)
                    }
            )




            k.forEach {
                itemDetailArray[it.first].removeAt(it.second)


                if (itemDetailArray[it.first].isEmpty()) {
                    val dataBase = db.writeDataBase()
                    val cv = ContentValues()
                    cv.put(autoAllItemArchive, 1)
                    dataBase!!.update(autoIncrementTable, cv, "$autoIncrementNumber=?", arrayOf(map[it]!!.id!!.toString()))
                }
            }
            expandList!!.notifyDataSetChanged()
            map.clear()
        }catch (e:Exception){
            e.printStackTrace()
        }







    }

    private fun timer() {
        resend=false
        object : CountDownTimer(10000, 1000) {
            override fun onFinish() {
                resend = true
            }
            override fun onTick(millisUntilFinished: Long) {
                Log.e(TAG,"second is $millisUntilFinished")
                    }

        }.start()
    }
    private  val TAG = "ForeGroundService"


}
