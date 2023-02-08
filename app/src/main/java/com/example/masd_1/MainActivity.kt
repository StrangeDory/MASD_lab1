package com.example.masd_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masd_1.DB.Adapter
import com.example.masd_1.DB.DBManager
import com.example.masd_1.DB.ListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val dbManager: DBManager = DBManager(this)
    val adapter = Adapter(ArrayList(), this)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MainStyle)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecycleView()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        dbManager.openDB()
        fillAdapter("")
    }

    fun onClickNew(view: View) {
        val newActivity = Intent(this, EditActivity::class.java)
        startActivity(newActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDB()
    }

    fun initRecycleView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    fun initSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fillAdapter(newText!!)
                return true
            }
        })
    }

    private fun fillAdapter(text: String) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = dbManager.readDBData(text)
            adapter.updateAdapter(list)
            checkTextNoElements(list)
        }
    }

    private fun checkTextNoElements(list: ArrayList<ListItem>) {
        if (list.size > 0) {
            val textNoElements = findViewById<TextView>(R.id.textViewNoElements)
            textNoElements.visibility = View.GONE
        }
        else {
            val textNoElements = findViewById<TextView>(R.id.textViewNoElements)
            textNoElements.visibility = View.VISIBLE
        }
    }
}