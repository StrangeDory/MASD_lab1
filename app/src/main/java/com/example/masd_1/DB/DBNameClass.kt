package com.example.masd_1.DB

import android.provider.BaseColumns

object DBNameClass : BaseColumns {
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "Notebook.db"

    const val TABLE_NAME = "notebook_entity"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_DATE = "date"

    const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_CONTENT TEXT)" +
            "$COLUMN_NAME_DATE TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}