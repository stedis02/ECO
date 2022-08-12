package com.example.tpueco.data.db

class ConstantsDB {
    companion object {
        const val DBName : String ="ecoDataBase"
        // constants for the table for storing data about the token
        const val accessToken: String = "access_token"
        const val refreshToken: String = "refresh_token"
        const val expiresIn: String = "expires_in"
        const val TokenDataTableName: String = "TokenData"
        const val TokenTableId: String = "_id";
        const val TokenDataTableStructure: String =
            "CREATE TABLE " + TokenDataTableName + " (" + TokenTableId + " INTEGER PRIMARY KEY," +
                accessToken + " TEXT," +
                refreshToken + " TEXT," +
                expiresIn + " Text)"
        const val TokenDataTableDrop: String = "DROP TABLE IF EXISTS $TokenDataTableName"
        // table constants for storing names of PDF documents
        const val pdfDocumentTableName = "pdfDocumentName"
        const val pdfDocumentName: String = "pdfDocumentName"
        const val pdfDocumentTableId: String = "_id"
        const val pdfDocumentTableStructure: String =
            "CREATE TABLE " + pdfDocumentTableName + " (" + pdfDocumentTableId + " INTEGER PRIMARY KEY," +
                    pdfDocumentName + " Text)"
        const val pdfDocumentTableDrop: String = "DROP TABLE IF EXISTS $pdfDocumentTableName"
            // table constants for storing message groups
             const val massageGroupsTableName: String = "MessageGroups"
        const val fromAdress: String = "fromAdress"
        const val lastMessageHeader: String = "lastHeader"
         const val lastMessageDate: String = "lastMessageData"
        const val massageGroupsTableId: String = "_id"
        const val massageGroupsTableStructure: String =
            "CREATE TABLE " + massageGroupsTableName + " (" + massageGroupsTableId + " INTEGER PRIMARY KEY," +
                    lastMessageHeader + " TEXT," +
                    lastMessageDate + " TEXT," +
                    fromAdress + " Text)"
        const val massageGroupsTableDrop: String = "DROP TABLE IF EXISTS $massageGroupsTableName"
        // do not forget to change the version when changes
        const val DBVersion: Int = 7;
    }
}