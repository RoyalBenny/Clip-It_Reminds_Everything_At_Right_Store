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
package com.clipit.cliptit2.databaseDirectory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.clipit.cliptit2.globalVariablesDirectory.ItemClass
import com.clipit.cliptit2.globalVariablesDirectory.ShopClass
import java.util.*
import kotlin.collections.ArrayList

const val jsonDataBase="MainDataBase"
const val jsonTableName="JsonTable"
const val autoDateColumn="autodateColumn"
const val autoItemCategoryColumn="autoItemCatColumn"
const val autoAllItemsBrought ="autoAllItemsBrought"
const val autoAllItemsDeleted = "autoAllItemsDeleted"
const val jsonLatColumn="jsonlatColumn"
const val jsonLngColumn="jsonlngcolumn"
const val jsonShopNameCloumn="jsonshopNameColumn"
const val autoShopLocation="autoShopLocation"
const val itemTableName="ItemTable"
const val itemName="ItemName"
const val itemIdNumberColumn="itemColumnNumber"
const val itemChecked="itemChecked"
const val autoIncrementTable="autoIncrementTable"
const val autoIncrementNumber="autoIncrementNumber"
const val jsonIncrementNumber = "jsonIncrementNumber"
const val autoCheckedClm = "autoChecked"
const val autoLatClm = "autoLat"
const val autoLngClm = "autoLng"
const val autoAllItemArchive = "autoAllItemArchive"
const val deleteTableName="DeleteItemTable"
const val deleteItemName="DeleteItemName"
const val deleteItemIdNumberColumn="DeleteItemColumnNumber"
const val deleteItemChecked="DeleteItemChecked"
const val archiveTable = "ArchiveTable"
const val archiveItemNameColumn = "ArchiveItemNameColumn"
const val archiveIdColumn = "ArchiveIdColumn"
const val archiveCheckedColumn = "ArchiveCheckedColumn"
const val imageTable = "ImageTable"
const val imageNameColumn = "ImageNameColumn"
const val imageColumn = "ImageColumn"
const val jsonSpecifiedColumn = "jsonSpecifiedColumn"
class JsonDataBase (val context :Context) : SQLiteOpenHelper(context, jsonDataBase,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createIncrementTable= "CREATE TABLE IF NOT EXISTS $autoIncrementTable ($autoIncrementNumber INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$autoDateColumn VARCHAR , $autoShopLocation VARCHAR,$autoItemCategoryColumn VARCHAR,$autoCheckedClm INTEGER,$autoLatClm VARCHAR,$autoLngClm VARCHAR,$autoAllItemsBrought INTEGER,$autoAllItemsDeleted INTEGER,$autoAllItemArchive INTEGER)"
        val createTable="CREATE TABLE IF NOT EXISTS $jsonTableName ($jsonIncrementNumber INTEGER," +
                "$jsonLatColumn VARCHAR,$jsonLngColumn VARCHAR,$jsonShopNameCloumn VARCHAR,$jsonSpecifiedColumn INTEGER)"
        val createItemTable = "CREATE TABLE IF NOT EXISTS $itemTableName ($itemName VARCHAR, $itemIdNumberColumn INTEGER, $itemChecked INTEGER )"
        val createDeleteTable = "CREATE TABLE IF NOT EXISTS $deleteTableName ($deleteItemName VARCHAR, $deleteItemIdNumberColumn INTEGER, $deleteItemChecked INTEGER )"
        val createArchiveTable = "CREATE TABLE IF NOT EXISTS $archiveTable ($archiveItemNameColumn VARCHAR, $archiveIdColumn INTEGER, $archiveCheckedColumn INTEGER )"
        val createImageTable = "CREATE TABLE IF NOT EXISTS $imageTable ($imageNameColumn VARCHAR, $imageColumn BLOB)"
        db?.execSQL(createTable)
        db?.execSQL(createItemTable)
        db?.execSQL(createDeleteTable)
        db?.execSQL(createArchiveTable)
        db?.execSQL(createImageTable)
        db?.execSQL(createIncrementTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertAutoNew(date:String,place:String,category: String,lat:Double,lng:Double,checked: Int):Int{
        var id : Int =-1
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(autoDateColumn,date)
        cv.put(autoCheckedClm,checked)
        cv.put(autoLatClm,lat)
        cv.put(autoLngClm,lng)
        cv.put(autoShopLocation,place)
        cv.put(autoItemCategoryColumn,category)
        cv.put(autoAllItemsDeleted,0)
        cv.put(autoAllItemsBrought,0)
        cv.put(autoAllItemArchive,0)
        db.insert(autoIncrementTable,null,cv)
        db.close()
        val db2 = this.readableDatabase
        val query = "Select * from $autoIncrementTable where  $autoShopLocation like '$place' and $autoItemCategoryColumn like '$category' and $autoDateColumn like '$date'"
        val cursor = db2.rawQuery(query,null)
        if(cursor.moveToFirst()){
            do{

                id = cursor.getInt(cursor.getColumnIndex(autoIncrementNumber))


            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        return id
    }




    fun updateAuto(id:Int,category: String,date: String,location:String,lat: Double,lng: Double){

        val db = this.writableDatabase
        val cv =ContentValues()
        cv.put(autoIncrementNumber,id)
        cv.put(autoItemCategoryColumn,category)
        cv.put(autoDateColumn,date)
        cv.put(autoShopLocation,location)
        cv.put(autoLatClm,lat)
        cv.put(autoLngClm,lng)
        cv.put(autoCheckedClm,0)
        db.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(id.toString()))
        db.close()
    }






    fun insertJson (shop: ArrayList<ShopClass>, id :Int) :Long {
        val db =this.writableDatabase
        var result : Long? = null

        shop.forEach {
            val cv = ContentValues()
            cv.put(jsonIncrementNumber,id)
            cv.put(jsonLatColumn,it.latitude.toString())
            cv.put(jsonLngColumn,it.longitude.toString())
            cv.put(jsonShopNameCloumn,it.shopName)
            cv.put(jsonSpecifiedColumn,0)
            result = db.insert(jsonTableName,null,cv)


        }
        db.close()

        return result!!
    }


    fun insertItem(item : String,id: Int,checked :Int) :Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(itemName,item)
        cv.put(itemIdNumberColumn,id)
        cv.put(itemChecked, checked)


        return db.insert(itemTableName,null,cv)
    }

    fun insertDeleteItem(itemArray: MutableCollection<ItemClass>){
        val db = this.writableDatabase
        itemArray.forEach {
            val cv = ContentValues()
            cv.put(deleteItemName,it.itemName)
            cv.put(deleteItemIdNumberColumn,it.id)
            cv.put(deleteItemChecked, it.checked)
            db.insert(deleteTableName,null,cv)
            db.delete(itemTableName,"$itemIdNumberColumn=? and $itemName=?", arrayOf(it.id.toString(),it.itemName))
        }

        db.close()

    }
    fun  writeDataBase(): SQLiteDatabase? {
        return this.writableDatabase
    }


    fun insertArchive(values: MutableCollection<ItemClass>) {
        val db = this.writableDatabase
        values.forEach{
            val cv = ContentValues()
            cv.put(archiveItemNameColumn,it.itemName)
            cv.put(archiveIdColumn,it.id)
            cv.put(archiveCheckedColumn, it.checked)

            db.insert(archiveTable,null,cv)
            db.delete(itemTableName,"$itemIdNumberColumn=? and $itemName=?", arrayOf(it.id.toString(),it.itemName))
        }
        db.close()

    }


    fun restoreFromArchive(items: ItemClass){
        val db = this.writableDatabase
        db.delete(archiveTable,"$archiveItemNameColumn=? and $archiveIdColumn=?", arrayOf(items.itemName,items.id.toString()))
        val cv = ContentValues()
        cv.put(itemName,items.itemName)
        cv.put(itemIdNumberColumn,items.id)
        cv.put(itemChecked,items.checked)
        db.insert(itemTableName,null,cv)
        cv.clear()
        cv.put(autoAllItemArchive,0)
        db.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(items.id!!.toString()))
        db.close()


    }



    fun readItemFromId(id : Int) : ArrayList<ItemClass>{
        val db = this.readableDatabase
        val query = "Select * from $itemTableName where $itemIdNumberColumn like $id"
        val itemArray = ArrayList<ItemClass>()
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()){
            do{
                val tempItem  = ItemClass()
                tempItem.itemName = cursor.getString(cursor.getColumnIndex(itemName))
                tempItem.id = cursor.getString(cursor.getColumnIndex(itemIdNumberColumn)).toInt()
                tempItem.checked = cursor.getInt(cursor.getColumnIndex(itemChecked))
                itemArray.add(tempItem)

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemArray
    }

    fun readJsonFromId(id : Int) : ArrayList<ShopClass>{
        val db = this.readableDatabase
        val query = "Select * from $jsonTableName where $jsonIncrementNumber like $id"
        val itemArray = ArrayList<ShopClass>()
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()){
            do{
                val tempItem  = ShopClass()
                tempItem.id = cursor.getString(cursor.getColumnIndex(jsonIncrementNumber)).toInt()
                tempItem.shopName= cursor.getString(cursor.getColumnIndex(jsonShopNameCloumn))
                tempItem.latitude = cursor.getString(cursor.getColumnIndex(jsonLatColumn)).toDouble()
                tempItem.longitude = cursor.getString(cursor.getColumnIndex(jsonLngColumn)).toDouble()
                tempItem.specfied = cursor.getInt(cursor.getColumnIndex(jsonSpecifiedColumn))
                itemArray.add(tempItem)

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemArray
    }



    fun returnIdBasedOnDateAndCate(date:String,category: String):ArrayList<ShopClass>{
        val data : ArrayList<ShopClass> =  arrayListOf()
        val db = this.readableDatabase
        val query = "Select * from $autoIncrementTable where $autoDateColumn like '$date' and $autoItemCategoryColumn like '$category'"
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()){
            do{
                val tempAddress= ShopClass()
                tempAddress.shopCategory = cursor.getString(cursor.getColumnIndex(autoItemCategoryColumn))
                tempAddress.shopLocation = cursor.getString(cursor.getColumnIndex(autoShopLocation))
                tempAddress.id = cursor.getString(cursor.getColumnIndex(autoIncrementNumber)).toInt()
                tempAddress.date = cursor.getString(cursor.getColumnIndex(autoDateColumn))
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(autoLatClm)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(autoLngClm)).toDouble()
                tempAddress.delete = cursor.getInt(cursor.getColumnIndex(autoAllItemsDeleted))
                tempAddress.archive = cursor.getInt(cursor.getColumnIndex(autoAllItemArchive))
                tempAddress.brought = cursor.getInt(cursor.getColumnIndex(autoAllItemsBrought))
                data.add(tempAddress)
            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()
        return data
    }



    fun returnItemBasedOnId(id:Int):ArrayList<ItemClass>{
        val data : ArrayList<ItemClass> =  arrayListOf()
        val db = this.readableDatabase
        val query = "Select * from $itemTableName where $itemIdNumberColumn like $id"
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()){
            do{
                val itemId= cursor.getInt(cursor.getColumnIndex(itemIdNumberColumn))
                val item =cursor.getString(cursor.getColumnIndex(itemName))
                val checked = cursor.getInt(cursor.getColumnIndex(itemChecked))
                data.add(ItemClass(item, itemId, checked))
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()

        return data
    }


    fun returnJsonBasedOnId(id:Int) :ArrayList<ShopClass>{

        val db= this.readableDatabase
        val list = ArrayList<ShopClass>()

        val query = "Select * from $jsonTableName where $jsonIncrementNumber like $id"
        val cursor= db.rawQuery(query,null)
        if(cursor.moveToFirst()){

            do{

                val tempAddress= ShopClass()
                tempAddress.shopName = cursor.getString(cursor.getColumnIndex(jsonShopNameCloumn))
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(jsonLatColumn)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(jsonLngColumn)).toDouble()
                tempAddress.id = cursor.getString(cursor.getColumnIndex(jsonIncrementNumber)).toInt()
                tempAddress.specfied = cursor.getInt(cursor.getColumnIndex(jsonSpecifiedColumn))
                list.add(tempAddress)

            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return list


    }



    fun returnIdBasedOnDate(date: String):ArrayList<ShopClass> {
        val db = this.readableDatabase
        val list = ArrayList<ShopClass>()
        val query = "Select * from $autoIncrementTable where $autoDateColumn like '$date'"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {

            do {

                val tempAddress= ShopClass()
                tempAddress.date = cursor.getString(cursor.getColumnIndex(autoDateColumn))
                tempAddress.delete = cursor.getInt(cursor.getColumnIndex(autoAllItemsDeleted))
                tempAddress.archive = cursor.getInt(cursor.getColumnIndex(autoAllItemArchive))
                if( tempAddress.delete == 1 || tempAddress.archive == 1) continue
                tempAddress.shopCategory = cursor.getString(cursor.getColumnIndex(autoItemCategoryColumn))
                tempAddress.shopLocation = cursor.getString(cursor.getColumnIndex(autoShopLocation))
                tempAddress.id = cursor.getString(cursor.getColumnIndex(autoIncrementNumber)).toInt()
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(autoLatClm)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(autoLngClm)).toDouble()
                tempAddress.brought = cursor.getInt(cursor.getColumnIndex(autoAllItemsBrought))
                list.add(tempAddress)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return list

    }


    fun returnAutoBasedOnDate(date:String):ArrayList<ShopClass>{
        val db= this.readableDatabase
        val list = ArrayList<ShopClass>()

        val query = "Select * from $autoIncrementTable where $autoDateColumn like '$date'"
        val cursor= db.rawQuery(query,null)
        if(cursor.moveToFirst()){

            do{

                val tempAddress= ShopClass()
                tempAddress.shopCategory = cursor.getString(cursor.getColumnIndex(autoItemCategoryColumn))
                tempAddress.shopLocation = cursor.getString(cursor.getColumnIndex(autoShopLocation))
                tempAddress.id = cursor.getString(cursor.getColumnIndex(autoIncrementNumber)).toInt()
                tempAddress.date = cursor.getString(cursor.getColumnIndex(autoDateColumn))
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(autoLatClm)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(autoLngClm)).toDouble()
                tempAddress.delete = cursor.getInt(cursor.getColumnIndex(autoAllItemsDeleted))
                tempAddress.brought = cursor.getInt(cursor.getColumnIndex(autoAllItemsBrought))
                list.add(tempAddress)

            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return list


    }



    fun updateItemTableWhenChecked(id:Int,item:String,checked: Int){

        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(itemIdNumberColumn,id)
        cv.put(itemName,item)
        cv.put(itemChecked,checked)
        db.update(itemTableName,cv,"$itemIdNumberColumn=? and $itemName=?", arrayOf(id.toString(),item))
        db.close()

    }

    fun returnAutoBasedOnTodayAndFurther():ArrayList<ShopClass>{
        val db= this.readableDatabase
        val list = ArrayList<ShopClass>()
        val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val today  = Calendar.getInstance().time
        val query = "Select * from $autoIncrementTable"
        val cursor= db.rawQuery(query,null)
        if(cursor.moveToFirst()){

            do{

                val tempAddress= ShopClass()
                tempAddress.date = cursor.getString(cursor.getColumnIndex(autoDateColumn))
                tempAddress.delete = cursor.getInt(cursor.getColumnIndex(autoAllItemsDeleted))
                tempAddress.archive = cursor.getInt(cursor.getColumnIndex(autoAllItemArchive))
                if(dateFormat.parse(tempAddress.date)<dateFormat.parse(dateFormat.format(today)) || tempAddress.delete == 1 || tempAddress.archive == 1) continue
                tempAddress.shopCategory = cursor.getString(cursor.getColumnIndex(autoItemCategoryColumn))
                tempAddress.shopLocation = cursor.getString(cursor.getColumnIndex(autoShopLocation))
                tempAddress.id = cursor.getString(cursor.getColumnIndex(autoIncrementNumber)).toInt()
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(autoLatClm)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(autoLngClm)).toDouble()
                tempAddress.brought = cursor.getInt(cursor.getColumnIndex(autoAllItemsBrought))
                list.add(tempAddress)

            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return list

    }


    fun viewDelete(): MutableMap<Int, MutableList<ItemClass>> {

        val db= this.readableDatabase
        val list  :MutableMap<Int,MutableList<ItemClass>> = mutableMapOf()
        val set = mutableSetOf<Int>()
        val query = "Select * from $deleteTableName"
        var cursor= db.rawQuery(query,null)
        if(cursor.moveToFirst()){

            do{

                set.add(cursor.getInt(cursor.getColumnIndex(deleteItemIdNumberColumn)))

            }while (cursor.moveToNext())

        }
        cursor.close()
        set.sortedDescending().forEach {
            cursor = db.rawQuery("Select * from $deleteTableName where $deleteItemIdNumberColumn like $it",null)
            val array = ArrayList<ItemClass>()
            if(cursor.moveToFirst()){
                do{
                    array.add(ItemClass(cursor.getString(cursor.getColumnIndex(deleteItemName)), cursor.getInt(cursor.getColumnIndex(deleteItemIdNumberColumn)), cursor.getInt(cursor.getColumnIndex(deleteItemChecked))))

                }while (cursor.moveToNext())

            }

            cursor.close()

            list[it] = array
        }
        db.close()

        return list

    }

    fun viewArchive(): MutableMap<Int, MutableList<ItemClass>> {

        val db= this.readableDatabase
        val list  :MutableMap<Int,MutableList<ItemClass>> = mutableMapOf()
        val set = mutableSetOf<Int>()
        val query = "Select * from $archiveTable"
        var cursor= db.rawQuery(query,null)
        if(cursor.moveToFirst()){

            do{

                set.add(cursor.getInt(cursor.getColumnIndex(archiveIdColumn)))
            }while (cursor.moveToNext())

        }
        cursor.close()
        set.sortedDescending().forEach {
            cursor = db.rawQuery("Select * from $archiveTable where $archiveIdColumn like $it",null)
            val array = ArrayList<ItemClass>()
            if(cursor.moveToFirst()){
                do{
                    array.add(ItemClass(cursor.getString(cursor.getColumnIndex(archiveItemNameColumn)), cursor.getInt(cursor.getColumnIndex(archiveIdColumn)), cursor.getInt(cursor.getColumnIndex(archiveCheckedColumn))))

                }while (cursor.moveToNext())

            }

            cursor.close()

            list[it] = array
        }
        db.close()

        return list

    }

    fun returnCategoryBasedOnId(id:Int): String? {
        val db = this.readableDatabase
        var category  = ""
        val cursor= db.rawQuery("Select * from $autoIncrementTable where $autoIncrementNumber like $id",null)
        if(cursor.moveToFirst()){

            category = cursor.getString(cursor.getColumnIndex(autoItemCategoryColumn))
            cursor.close()
        }
        db.close()


        return category
    }

    fun returnAutoBasedOnRecent():ArrayList<ShopClass>{
        val db= this.readableDatabase
        val list = ArrayList<ShopClass>()
        val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val today  = Calendar.getInstance().time
        val query = "Select * from $autoIncrementTable"
        val cursor= db.rawQuery(query,null)
        if(cursor.moveToFirst()){

            do{

                val tempAddress= ShopClass()
                tempAddress.date = cursor.getString(cursor.getColumnIndex(autoDateColumn))
                tempAddress.delete = cursor.getInt(cursor.getColumnIndex(autoAllItemsDeleted))
                tempAddress.archive = cursor.getInt(cursor.getColumnIndex(autoAllItemArchive))
                if(dateFormat.parse(tempAddress.date)>=dateFormat.parse(dateFormat.format(today)) || tempAddress.delete == 1 || tempAddress.archive == 1) continue
                tempAddress.shopCategory = cursor.getString(cursor.getColumnIndex(autoItemCategoryColumn))
                tempAddress.shopLocation = cursor.getString(cursor.getColumnIndex(autoShopLocation))
                tempAddress.id = cursor.getString(cursor.getColumnIndex(autoIncrementNumber)).toInt()
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(autoLatClm)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(autoLngClm)).toDouble()
                tempAddress.brought = cursor.getInt(cursor.getColumnIndex(autoAllItemsBrought))
                list.add(tempAddress)

            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return list


    }


    fun returnJsonBasedOnIdAfterFilterSpecify(id:Int) :ArrayList<ShopClass>{

        val db= this.readableDatabase
        val list = ArrayList<ShopClass>()

        val query = "Select * from $jsonTableName where $jsonIncrementNumber like $id and $jsonSpecifiedColumn like 1"
        val cursor= db.rawQuery(query,null)

        if(cursor.moveToFirst()){

            do{

                val tempAddress= ShopClass()
                tempAddress.shopName = cursor.getString(cursor.getColumnIndex(jsonShopNameCloumn))
                tempAddress.latitude = cursor.getString(cursor.getColumnIndex(jsonLatColumn)).toDouble()
                tempAddress.longitude = cursor.getString(cursor.getColumnIndex(jsonLngColumn)).toDouble()
                tempAddress.id = cursor.getString(cursor.getColumnIndex(jsonIncrementNumber)).toInt()
                tempAddress.specfied = cursor.getInt(cursor.getColumnIndex(jsonSpecifiedColumn))
                list.add(tempAddress)

            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return list


    }



    fun restoreDelete(item: ItemClass){

        val db = this.writeDataBase()
        db!!.delete(deleteTableName,"$deleteItemName=? and $deleteItemIdNumberColumn=?", arrayOf(item.itemName,item.id.toString()))
        val cv =ContentValues()
        cv.put(itemIdNumberColumn,item.id)
        cv.put(itemChecked,item.checked)
        cv.put(itemName,item.itemName)
        db.insert(itemTableName,null,cv)
        cv.clear()
        cv.put(autoAllItemsDeleted,0)
        db.update(autoIncrementTable,cv,"$autoIncrementNumber=?", arrayOf(item.id!!.toString()))
        db.close()
    }



    fun deletePermanently(item: ItemClass){

        val db = this.writeDataBase()
        db!!.delete(deleteTableName,"$deleteItemName=? and $deleteItemIdNumberColumn=?", arrayOf(item.itemName,item.id.toString()))
        db.close()
    }


    fun insertImage(name : String , Image : ByteArray){
        val db = this.writeDataBase()
        val cv = ContentValues()
        cv.put(imageNameColumn,name)
        cv.put(imageColumn,Image)
        db!!.insert(imageTable,null,cv)
        db.close()
    }

    fun readImage(name : String):ByteArray{
        val db = this.readableDatabase
        var image :ByteArray? =null
        val cursor = db!!.rawQuery("Select * from $imageTable where $imageNameColumn like '$name'",null)
        if(cursor.moveToLast()){
            image = cursor.getBlob(cursor.getColumnIndex(imageColumn))

        }
        cursor.close()
        db.close()

        return image!!
    }




}