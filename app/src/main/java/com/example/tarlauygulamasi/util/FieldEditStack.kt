package com.example.tarlauygulamasi.util

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

sealed class FieldEditStack {

    data class AddPoint(val point: LatLng,val marker: Marker?) : FieldEditStack()
    data class MovePoint(val oldPoint: LatLng, val newPoint: LatLng,val index:Int) : FieldEditStack()

}