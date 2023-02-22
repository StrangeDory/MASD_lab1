package com.example.masd_1.DB

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DAOListItem() {
    private var databaseReference: DatabaseReference

    init {
        val db :FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference(ListItem::class.simpleName!!)
    }

    fun add() : String {
        return  databaseReference.push().key!!
    }

    fun setValue(key: String,item: ListItem) : Task<Void> {
        return databaseReference.child(key).setValue(item)
    }

    fun remove(key: String) : Task<Void> {
        return databaseReference.child(key).removeValue()
    }
}