package com.example.firebaserealtimedatabase.Models

class Message {
    var message:String? = null
    var date:String? = null
    var fromUid:String? = null
    var toUid:String? = null



    constructor()
    constructor(message: String?, date: String?, fromUid: String?, toUid: String?) {
        this.message = message
        this.date = date
        this.fromUid = fromUid
        this.toUid = toUid
    }
}