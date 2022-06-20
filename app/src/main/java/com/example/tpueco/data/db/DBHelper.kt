package com.example.tpueco.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, ConstantsDB.DBName, null, ConstantsDB.DBVersion) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL(ConstantsDB.TokenDataTableStructure)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        sqLiteDatabase?.execSQL(ConstantsDB.TokenDataTableStructure)
        onCreate(sqLiteDatabase)
    }

}