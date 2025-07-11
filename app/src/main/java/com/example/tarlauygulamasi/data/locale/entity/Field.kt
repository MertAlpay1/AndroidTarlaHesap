package com.example.tarlauygulamasi.data.locale.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    val id: Long,
    val name:String="",
    val area: Double=0.0,
    val pointList: ArrayList<String> = arrayListOf(),
    @ColumnInfo("userId")
    val userId: String,

    )
