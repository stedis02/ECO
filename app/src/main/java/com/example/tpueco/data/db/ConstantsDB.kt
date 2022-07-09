package com.example.tpueco.data.db

class ConstantsDB {
    companion object {
        val DBName : String ="db.db.db"
        // константы  для таблицы для хранения данных о токене
        val access_token: String = "access_token"
        val refresh_token: String = "refresh_token"
        val expires_in: String = "expires_in"
        val TokenDataTableName: String = "TokenData"
        val TokenTableId: String = "_id";
        val TokenDataTableStructure: String =
            "CREATE TABLE " + TokenDataTableName + " (" + TokenTableId + " INTEGER PRIMARY KEY," +
                access_token + " TEXT," +
                refresh_token + " TEXT," +
                expires_in + " Text)"
        val TokenDataTableDrop: String = "DROP TABLE IF EXISTS $TokenDataTableName"
        // константы  для таблицы для хранения имён PDF  документов
        val pdfDocumentTableName: String = "pdfDocumentName"
        val pdfDocumentName: String = "pdfDocumentName"
        val pdfDocumentTableId: String = "_id"
        val pdfDocumentTableStructure: String =
            "CREATE TABLE " + pdfDocumentTableName + " (" + pdfDocumentTableId + " INTEGER PRIMARY KEY," +
                    pdfDocumentName + " Text)"
        val pdfDocumentTableDrop: String = "DROP TABLE IF EXISTS $pdfDocumentTableName"
        // не забывать менять версию при изменениях
        val DBVersion: Int = 4;
    }
}