package com.example.tarlauygulamasi.data.locale.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.tarlauygulamasi.data.locale.entity.Field

@Dao
interface FieldDao {

    @Insert
    suspend fun insertField(field: Field)

    @Delete
    suspend fun deleteField(field: Field)

}