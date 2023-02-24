package com.example.masd_1.DB

class ListItem {
    var title: String = ""
        get() = field
        set(value) { field = value}
    var content: String = ""
        get() = field
        set(value) { field = value}
    var date: String = ""
        get() = field
        set(value) { field = value}
    var id: Int = 1
        get() = field
        set(value) { field = value}
    var idFirebase: String = ""
        get() = field
        set(value) { field = value}

    constructor(title: String, content: String, date: String, id: Int, idFirebase: String) {
        this.title = title
        this.content = content
        this.date = date
        this.id = id
        this.idFirebase = idFirebase
    }

    constructor() {}


}