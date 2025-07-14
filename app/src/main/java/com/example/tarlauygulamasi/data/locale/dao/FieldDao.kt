package com.example.tarlauygulamasi.data.locale.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tarlauygulamasi.data.locale.entity.Field
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {

    @Insert
    suspend fun insertField(field: Field)

    @Delete
    suspend fun deleteField(field: Field)

    @Query("SELECT * FROM field  WHERE userId=:userId")
    fun getFieldByUserId(userId: String): Flow<List<Field>>

}