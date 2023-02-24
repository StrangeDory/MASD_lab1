package com.example.masd_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.masd_1.DB.DBManager
import com.example.masd_1.DB.IntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class EditActivity : AppCompatActivity() {

    val dbManager = DBManager(this)
    var id: Int = 0
    var idFirebase = ""
    var isEditState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.EditStyle)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        getIntents()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        dbManager.openDB()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDB()
    }

    fun saveData() {
        if (checkNotEmpty())
        {
            val editTextTitleTable = findViewById<EditText>(R.id.editTextTitle)
            val editTextContentTable = findViewById<EditText>(R.id.editTextContent)
            val title = editTextTitleTable.text.toString()
            val content = editTextContentTable.text.toString()
            val date = getCurrentDateTime()
            CoroutineScope(Dispatchers.Main).launch {
                if (isEditState)
                    dbManager.updateDB(title, content, date, id, idFirebase)
                else
                    dbManager.insertToDB(title, content, date)
                isEditState = false
                finish()
            }
        }
        if (!checkNotEmpty() && isEditState) {
            dbManager.removeFromDB(id.toString(), idFirebase)
            isEditState = false
            finish()
        }
    }

    fun onClickBackMain(view: View) {
        saveData()
    }

    fun getCurrentDateTime(): String {
        return LocalDateTime.now().toString()
    }

    fun checkNotEmpty() : Boolean {
        val editTextTitleTable = findViewById<EditText>(R.id.editTextTitle)
        val editTextContentTable = findViewById<EditText>(R.id.editTextContent)
        val title = editTextTitleTable.text.toString()
        val content = editTextContentTable.text.toString()
        return title != "" || content != ""
    }

    fun getIntents() {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(IntentConstants.INTENT_TITLE_KEY) != null || i.getStringExtra(IntentConstants.INTENT_CONTENT_KEY) != null) {
                val editTextTitleTable = findViewById<EditText>(R.id.editTextTitle)
                val editTextContentTable = findViewById<EditText>(R.id.editTextContent)
                if (i.getStringExtra(IntentConstants.INTENT_TITLE_KEY) != null)
                    editTextTitleTable.setText(i.getStringExtra(IntentConstants.INTENT_TITLE_KEY))
                if (i.getStringExtra(IntentConstants.INTENT_CONTENT_KEY) != null)
                    editTextContentTable.setText(i.getStringExtra(IntentConstants.INTENT_CONTENT_KEY))
                id = i.getIntExtra(IntentConstants.INTENT_ID_KEY,0 )
                if (i.getStringExtra(IntentConstants.INTENT_ID_FIREBASE_KEY) != null)
                    idFirebase = i.getStringExtra(IntentConstants.INTENT_ID_FIREBASE_KEY)!!
                isEditState = true
            }
        }
    }
}