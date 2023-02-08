package com.example.masd_1.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.masd_1.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DBManager(context: Context) {
    val dbFunctions = DBFunctions(context)
    var db: SQLiteDatabase? = null

    fun openDB() {
        db = dbFunctions.writableDatabase
    }

    suspend fun insertToDB(title: String, content: String, date: String) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(DBNameClass.COLUMN_NAME_TITLE, title)
            put(DBNameClass.COLUMN_NAME_CONTENT, content)
            put(DBNameClass.COLUMN_NAME_DATE, date)
        }
        db?.insert(DBNameClass.TABLE_NAME, null, values)
    }

    fun removeFromDB(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(DBNameClass.TABLE_NAME, selection, null)
    }

    suspend fun readDBData(searchText: String) : ArrayList<ListItem> = withContext(Dispatchers.IO) {
        val dataList = ArrayList<ListItem>()
        val selection = "${DBNameClass.COLUMN_NAME_TITLE} LIKE ? OR ${DBNameClass.COLUMN_NAME_CONTENT} LIKE ?"
        val cursor = db?.query(DBNameClass.TABLE_NAME, null, selection, arrayOf("%$searchText%", "%$searchText%"), null, null, "${DBNameClass.COLUMN_NAME_DATE} DESC")
        while (cursor?.moveToNext()!!) {
            val dataTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getString(cursor.getColumnIndexOrThrow(DBNameClass.COLUMN_NAME_CONTENT))
            val dataDate = cursor.getString(cursor.getColumnIndexOrThrow(DBNameClass.COLUMN_NAME_DATE))
            val dataId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val item = ListItem()
            item.title = dataTitle
            item.content = dataContent
            item.date = dataDate
            item.id = dataId
            dataList.add(item)
        }
        cursor.close()
        return@withContext dataList
    }

    suspend fun updateDB(title: String, content: String, date: String, id: Int) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(DBNameClass.COLUMN_NAME_TITLE, title)
            put(DBNameClass.COLUMN_NAME_CONTENT, content)
            put(DBNameClass.COLUMN_NAME_DATE, date)
        }
        val selection = BaseColumns._ID + "=$id"
        db?.update(DBNameClass.TABLE_NAME, values, selection, null)
    }

    fun deleteDB() {
        dbFunctions.onDelete(db)
    }

    fun closeDB() {
        dbFunctions.close()
    }
}