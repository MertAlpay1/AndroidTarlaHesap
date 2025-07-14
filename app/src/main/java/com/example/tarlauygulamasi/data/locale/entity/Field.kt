package com.example.tarlauygulamasi.data.locale.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import org.checkerframework.checker.units.qual.Area

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"]
    )]
)
data class Field(

    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val name:String="",
    val area: Double=0.0,
    val pointList: MutableList<LatLng>,
    val userId: String,

    )
