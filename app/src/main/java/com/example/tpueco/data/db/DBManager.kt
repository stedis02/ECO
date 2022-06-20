package com.example.tpueco.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tpueco.domain.Model.UserTokenResponse
import com.example.tpueco.presentation.VM.MainViewModel

class DBManager(_context: Context) {
    lateinit var context: Context
    lateinit var dbHelper: DBHelper
    lateinit var sqLiteDatabase: SQLiteDatabase
    init {
        context = _context
        dbHelper = DBHelper(_context)
    }

    fun dbOpen(){
    sqLiteDatabase = dbHelper.writableDatabase
    }
    fun dbClose(){
        dbHelper.close()
    }
    //  инструменты для базы данных для токена
    fun dbInsertTokenData(access_token: String, refresh_token: String, expires_in: String){
         val contentValues = ContentValues()
        contentValues.put(ConstantsDB.access_token, access_token)
        contentValues.put(ConstantsDB.refresh_token, refresh_token)
        contentValues.put(ConstantsDB.expires_in, expires_in)
        sqLiteDatabase.insert(ConstantsDB.TokenDataTableName, null, contentValues)
    }
    fun dbUpdateTokenData(id: Int, access_token: String, refresh_token: String, expires_in: String){
        val contentValues = ContentValues()
        var selection : String = ConstantsDB.TokenTableId +"="+id
        contentValues.put(ConstantsDB.access_token, access_token)
        contentValues.put(ConstantsDB.refresh_token, refresh_token)
        contentValues.put(ConstantsDB.expires_in, expires_in)
        Log.e("aaaa", contentValues.toString())
        sqLiteDatabase.update(ConstantsDB.TokenDataTableName, contentValues,selection,null)
    }
    fun deleteTokenData(id:Int){
        var selection : String = ConstantsDB.TokenTableId +"="+id
        sqLiteDatabase.delete(ConstantsDB.TokenDataTableName, selection, null)
    }
    fun getTokenData(): UserTokenResponse{
        var userTokenResponse = UserTokenResponse()
        var cursor : Cursor? = sqLiteDatabase.query(ConstantsDB.TokenDataTableName, null, null,null, null,null, null)
        if (cursor != null) {
            while (cursor.moveToNext()){
                userTokenResponse.access_token = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.access_token))
                userTokenResponse.refresh_token = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.refresh_token))
                userTokenResponse.expires_in = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.expires_in))
            }
        }
        cursor?.close()
        return userTokenResponse
    }
}