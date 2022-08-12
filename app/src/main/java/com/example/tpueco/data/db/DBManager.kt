package com.example.tpueco.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tpueco.domain.Model.Message
import com.example.tpueco.domain.Model.MessageDate
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
        contentValues.put(ConstantsDB.accessToken, access_token)
        contentValues.put(ConstantsDB.refreshToken, refresh_token)
        contentValues.put(ConstantsDB.expiresIn, expires_in)
        sqLiteDatabase.insert(ConstantsDB.TokenDataTableName, null, contentValues)
    }
    fun dbInsertMessageGroup(from: String, lastMessageHeader: String, lastMessageDate: String){
        val contentValues = ContentValues()
        contentValues.put(ConstantsDB.fromAdress, from)
        contentValues.put(ConstantsDB.lastMessageDate, lastMessageDate)
        contentValues.put(ConstantsDB.lastMessageHeader, lastMessageHeader)
        sqLiteDatabase.insert(ConstantsDB.massageGroupsTableName, null, contentValues)
    }

    fun dbInsertPdfDocument(pdfDocumentName: String){
        val contentValues = ContentValues()
        contentValues.put(ConstantsDB.pdfDocumentName, pdfDocumentName)
        sqLiteDatabase.insert(ConstantsDB.pdfDocumentTableName, null, contentValues)
    }

    fun dbUpdateTokenData(id: Int, access_token: String, refresh_token: String, expires_in: String){
        val contentValues = ContentValues()
        var selection : String = ConstantsDB.TokenTableId +"="+id
        contentValues.put(ConstantsDB.accessToken, access_token)
        contentValues.put(ConstantsDB.refreshToken, refresh_token)
        contentValues.put(ConstantsDB.expiresIn, expires_in)
        sqLiteDatabase.update(ConstantsDB.TokenDataTableName, contentValues,selection,null)
    }
    fun dbUpdateMessageGroup(id: Int, from: String, lastMessageHeader: String, lastMessageDate: String){
        val contentValues = ContentValues()
        var selection : String = ConstantsDB.massageGroupsTableId +"="+id
        contentValues.put(ConstantsDB.fromAdress, from)
        contentValues.put(ConstantsDB.lastMessageDate, lastMessageDate)
        contentValues.put(ConstantsDB.lastMessageHeader, lastMessageHeader)
        sqLiteDatabase.update(ConstantsDB.massageGroupsTableName, contentValues,selection,null)
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

    fun deleteMessageGroup(id:Int){
        var selection : String = ConstantsDB.massageGroupsTableId +"="+id
        sqLiteDatabase.delete(ConstantsDB.massageGroupsTableName, selection, null)
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
                userTokenResponse.access_token = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.accessToken))
                userTokenResponse.refresh_token = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.refreshToken))
                userTokenResponse.expires_in = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.expiresIn))
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

    fun getMessageGroupsList(): MutableList<Message>{

        var messageGroup: MutableList<Message> = mutableListOf()
        var cursor : Cursor? = sqLiteDatabase.query(ConstantsDB.massageGroupsTableName, null, null,null, null,null, null)
        if (cursor != null) {
            while (cursor.moveToNext()){
                var from = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.fromAdress))
                var lastMessageHeader = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.lastMessageHeader))
                var lastMessageDate = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsDB.lastMessageDate))
                val date = MessageDate("year", "month", lastMessageDate, "DOW", "time")
                messageGroup.add(Message(from, lastMessageHeader,"text", date))
            }
        }
        cursor?.close()
        return messageGroup
    }
}