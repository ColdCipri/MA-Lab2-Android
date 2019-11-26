package com.example.ma_lab2_android.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ma_lab2_android.Model.Meds

class DBHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object{
        private val DATABASE_VER = 1;
        private val DATABASE_NAME = "meds.db"

        //Table
        private val TABLE_NAME = "Med"
        private val COL_ID = "Id"
        private val COL_Name = "Nume"
        private val COL_DataExpirarii = "Data_expirarii"
        private val COL_Pieces = "Bucati"
        private val COL_BaseSubst = "Substanta_baza"
        private val COL_QuantityBaseSubst = "Cantitate_subst_baza"
        private val COL_Description = "Descriere"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY : String = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY,$COL_Name TEXT," +
                "$COL_DataExpirarii TEXT,$COL_Pieces INTEGER,$COL_BaseSubst TEXT,$COL_QuantityBaseSubst TEXT,$COL_Description TEXT)" )
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }


    //CRUD
    val allMeds:List<Meds>
        get() {
            val listMeds = ArrayList<Meds>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst())
            {
                do{
                    val med = Meds()
                    med.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    med.name = cursor.getString(cursor.getColumnIndex(COL_Name))
                    med.dataExp = cursor.getString(cursor.getColumnIndex(COL_DataExpirarii))
                    med.pieces = cursor.getInt(cursor.getColumnIndex(COL_Pieces))
                    med.baseSubst = cursor.getString(cursor.getColumnIndex(COL_BaseSubst))
                    med.quantityBaseSubst = cursor.getString(cursor.getColumnIndex(COL_QuantityBaseSubst))
                    med.description = cursor.getString(cursor.getColumnIndex(COL_Description))

                    listMeds.add(med)
                } while (cursor.moveToNext())
            }
            db.close()
            return listMeds
        }

    fun addMed(med: Meds){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, med.id)
        values.put(COL_Name, med.name)
        values.put(COL_DataExpirarii, med.dataExp)
        values.put(COL_Pieces, med.pieces)
        values.put(COL_BaseSubst, med.baseSubst)
        values.put(COL_QuantityBaseSubst, med.quantityBaseSubst)
        values.put(COL_Description,med.description)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateMed(med: Meds): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, med.id)
        values.put(COL_Name, med.name)
        values.put(COL_DataExpirarii, med.dataExp)
        values.put(COL_Pieces, med.pieces)
        values.put(COL_BaseSubst, med.baseSubst)
        values.put(COL_QuantityBaseSubst, med.quantityBaseSubst)
        values.put(COL_Description,med.description)

        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(med.id.toString()))
    }

    fun deleteMed(med: Meds){
        val db = this.writableDatabase

        db.delete(TABLE_NAME,"$COL_ID=?", arrayOf(med.id.toString()))
        db.close()
    }
}