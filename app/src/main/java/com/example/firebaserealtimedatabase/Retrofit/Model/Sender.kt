package com.example.firebaserealtimedatabase.Retrofit.Model

data class Sender(val data:Data, val to:String)
data class Data(val user:String, val icon:Int, val body:String, val title:String, val sented:String)