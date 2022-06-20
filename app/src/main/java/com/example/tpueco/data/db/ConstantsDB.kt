package com.example.tpueco.data.db

class ConstantsDB {
    companion object {
        val DBName : String ="db.db.db"
        // таблица для хранения данных о токене
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

        // не забывать менять версию при изменениях
        val DBVersion: Int = 2;
    }
}