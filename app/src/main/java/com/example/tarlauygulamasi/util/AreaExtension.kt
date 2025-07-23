package com.example.tarlauygulamasi.util

    fun Double.toFormattedArea():String {
          return "%.2f dönüm".format(this/1000)
    }


    fun Double.toFormattedMeter():String{
        return "%.2f m".format(this)
    }
