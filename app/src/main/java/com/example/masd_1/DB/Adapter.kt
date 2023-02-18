package com.example.masd_1.DB

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.masd_1.EditActivity
import com.example.masd_1.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


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
            textViewTitle.setOnClickListener() {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(IntentConstants.INTENT_TITLE_KEY, item.title)
                    putExtra(IntentConstants.INTENT_CONTENT_KEY, item.content)
                    putExtra(IntentConstants.INTENT_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
            textViewContent.setOnClickListener() {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(IntentConstants.INTENT_TITLE_KEY, item.title)
                    putExtra(IntentConstants.INTENT_CONTENT_KEY, item.content)
                    putExtra(IntentConstants.INTENT_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
            textViewDate.setOnClickListener() {
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

    fun popup(holder: Holder) {
        val popupMenu = PopupMenu(context, holder.itemView)
        popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.item_delete) {
                removeItem(holder.layoutPosition)
            }
            if (id == R.id.item_share) {
                val summary = listMainLocal[holder.layoutPosition].title + "\n\n" + holder.transformDate(listMainLocal[holder.layoutPosition].date) + "\n" +
                        listMainLocal[holder.layoutPosition].content
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, summary)

                // Check if there's an app that can handle this intent before launching it
                if (context?.packageManager?.resolveActivity(intent, 0) != null) {
                    context.startActivity(intent)
                }
            }
            if (id == R.id.item_copy) {
                val summary = listMainLocal[holder.layoutPosition].title + "\n\n" + holder.transformDate(listMainLocal[holder.layoutPosition].date) + "\n" +
                        listMainLocal[holder.layoutPosition].content
                var clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText("", summary))
            }
            false
        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(listMainLocal[position])
        holder.itemView.setOnLongClickListener() {
            popup(holder)
            false
        }
        val textViewContent = holder.itemView.findViewById<TextView>(R.id.textViewContent)
        val textViewTitle = holder.itemView.findViewById<TextView>(R.id.textViewTitle)
        val textViewDate = holder.itemView.findViewById<TextView>(R.id.textViewDate)
        textViewContent.setOnLongClickListener() {
            popup(holder)
            false
        }
        textViewTitle.setOnLongClickListener() {
            popup(holder)
            false
        }
        textViewDate.setOnLongClickListener() {
            popup(holder)
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
