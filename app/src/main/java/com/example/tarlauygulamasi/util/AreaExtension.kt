package com.example.tarlauygulamasi.util

import java.text.NumberFormat
import java.util.Locale

fun Double.toFormattedArea():String {

        val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)
        formatter.maximumFractionDigits = 2
        return "${formatter.format(this / 1000)} dönüm"
    }


    fun Double.toFormattedMeter():String{
        return "%.2f m".format(this)
    }
