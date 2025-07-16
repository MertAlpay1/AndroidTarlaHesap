package com.example.tarlauygulamasi.util

    fun Double.toFormattedArea():String {

        val area= this
        return if (area < 10000) {
             "%.0f m²".format(area)
        } else {
            "%.2f hectare".format(area/10000)
        }
    }
