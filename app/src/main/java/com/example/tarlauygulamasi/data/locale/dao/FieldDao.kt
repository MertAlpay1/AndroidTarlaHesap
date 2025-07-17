package com.example.tarlauygulamasi.data.locale.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tarlauygulamasi.data.locale.entity.Field
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {

    @Insert
    suspend fun insertField(field: Field)

    @Update
    suspend fun updateField(field: Field)

    @Query("DELETE  FROM field WHERE field.id=:fieldId")
    suspend fun deleteFieldBy(fieldId: Long)

    @Query("SELECT * FROM field  WHERE userId=:userId")
    fun getFieldByUserId(userId: String): Flow<List<Field>>

    @Query("SELECT * FROM field WHERE field.id=:fieldId")
    suspend fun getFieldByFieldId(fieldId:Long): Field

}