package com.example.tarlauygulamasi.data.locale

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.android.gms.maps.model.LatLng


class Converters {

    @TypeConverter
    fun fromString(json:String): MutableList<LatLng> {
        val listType = object : TypeToken<MutableList<LatLng>>() {}.type
        val list = Gson().fromJson<MutableList<LatLng>>(json, listType)
        return list
    }

    @TypeConverter
    fun fromArrayList(list: MutableList<LatLng>): String {
        val gson= Gson()
        val json=gson.toJson(list)
        return json
    }

}