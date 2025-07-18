package com.example.tarlauygulamasi.util

    fun Double.toFormattedArea():String {
          return "%.2f dönüm".format(this/1000)
    }
