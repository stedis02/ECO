package com.example.tpueco.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tpueco.domain.Model.UserTokenResponse
import com.example.tpueco.domain.tools.Document.Document

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
    fun dbInsertTokenData(access_token: String, refresh_token: String, expires_in: String){
         val contentValues = ContentValues()
        contentValues.put(ConstantsDB.access_token, access_token)
        contentValues.put(ConstantsDB.refresh_token, refresh_token)
        contentValues.put(ConstantsDB.expires_in, expires_in)
        sqLiteDatabase.insert(ConstantsDB.TokenDataTableName, null, contentValues)
    }
    fun dbInsertPdfDocument(pdfDocumentName: String){
        val contentValues = ContentValues()
        contentValues.put(ConstantsDB.pdfDocumentName, pdfDocumentName)
        sqLiteDatabase.insert(ConstantsDB.pdfDocumentTableName, null, contentValues)
    }

    fun dbUpdateTokenData(id: Int, access_token: String, refresh_token: String, expires_in: String){
        val contentValues = ContentValues()
        var selection : String = ConstantsDB.TokenTableId +"="+id
        contentValues.put(ConstantsDB.access_token, access_token)
        contentValues.put(ConstantsDB.refresh_token, refresh_token)
        contentValues.put(ConstantsDB.expires_in, expires_in)
        sqLiteDatabase.update(ConstantsDB.TokenDataTableName, contentValues,selection,null)
    }

    fun dbUpdatePdfDocument(id: Int, pdfDocumentName: String) {
        val contentValues = ContentValues()
        var selection: String = ConstantsDB.pdfDocumentTableId + "=" + id
        contentValues.put(ConstantsDB.pdfDocumentName, pdfDocumentName)
        sqLiteDatabase.update(ConstantsDB.pdfDocumentTableName, contentValues, selection, null)
    }

    fun deleteTokenData(id:Int){
        var selection : String = ConstantsDB.TokenTableId +"="+id
        sqLiteDatabase.delete(ConstantsDB.TokenDataTableName, selection, null)
    }

    fun deletePdfDocument(id: String){
        var selection : String = ConstantsDB.pdfDocumentTableId +"="+id
        sqLiteDatabase.delete(ConstantsDB.pdfDocumentTableName, selection, null)
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

    fun getPdfDocument(): MutableList<Document>{

        var pdfDocumentGroup: MutableList<Document> = mutableListOf()
        var cursor : Cursor? = sqLiteDatabase.query(ConstantsDB.pdfDocumentTableName, null, null,null, null,null, null)
        if (cursor != null) {
            while (cursor.moveToNext()){
               var document = Document()
                document.pdfDocumentName = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.pdfDocumentName))
                document.documentId = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsDB.pdfDocumentTableId))
                pdfDocumentGroup.add(document)
            }
        }
        cursor?.close()
        return pdfDocumentGroup
    }
}