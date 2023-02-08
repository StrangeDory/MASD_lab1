package com.example.masd_1.DB

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masd_1.EditActivity
import com.example.masd_1.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList


class Adapter(listMain : ArrayList<ListItem>, contextMain: Context) : RecyclerView.Adapter<Adapter.Holder>() {

    var listMainLocal = listMain
    var context = contextMain


    class Holder(itemView: View, contextMain: Context) : RecyclerView.ViewHolder(itemView) {

        var context = contextMain
        val textViewContent = itemView.findViewById<TextView>(R.id.textViewContent)
        val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        val textViewDate = itemView.findViewById<TextView>(R.id.textViewDate)

        fun setData(item: ListItem) {
            textViewContent.text = item.content
            textViewTitle.text = item.title
            textViewDate.text =  transformDate(item.date)
            itemView.setOnClickListener() {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(IntentConstants.INTENT_TITLE_KEY, item.title)
                    putExtra(IntentConstants.INTENT_CONTENT_KEY, item.content)
                    putExtra(IntentConstants.INTENT_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }

        fun transformDate(dateText: String) : String {
            val date = LocalDateTime.parse(dateText)
            val dateNow = LocalDateTime.now()
            return if (date.year == dateNow.year && date.month == dateNow.month && date.dayOfMonth == dateNow.dayOfMonth) {
                date.format(DateTimeFormatter.ofPattern("kk:mm", Locale.ROOT)).toString()
            } else {
                date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.recycle_view_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(listMainLocal[position])
        holder.itemView.setOnLongClickListener() {
            val popupMenu = PopupMenu(context, holder.itemView)
            popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                val id = menuItem.itemId
                if (id == R.id.item_delete) {
                    removeItem(holder.layoutPosition)
                }
                false
            }
            popupMenu.show()
            false
        }
    }

    override fun getItemCount(): Int {
        return listMainLocal.size
    }

    fun updateAdapter(listItems: List<ListItem>) {
        listMainLocal.clear()
        listMainLocal.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int) {
        val dbManager = DBManager(context)
        dbManager.openDB()
        dbManager.removeFromDB(listMainLocal[pos].id.toString())
        dbManager.closeDB()
        listMainLocal.removeAt(pos)
        notifyItemRangeChanged(0, listMainLocal.size)
        notifyItemRemoved(pos)
    }
}
