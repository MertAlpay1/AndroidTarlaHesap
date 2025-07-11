package com.example.tarlauygulamasi.data.locale

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromString(json:String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        val list = Gson().fromJson<ArrayList<String>>(json, listType)
        return list
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        val gson= Gson()
        val json=gson.toJson(list)
        return json
    }

}