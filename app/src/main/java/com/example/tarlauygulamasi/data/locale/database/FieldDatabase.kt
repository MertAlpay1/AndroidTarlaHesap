package com.example.tarlauygulamasi.data.locale.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tarlauygulamasi.data.database.UserDatabase
import com.example.tarlauygulamasi.data.locale.dao.FieldDao
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.data.locale.Converters
import com.example.tarlauygulamasi.data.locale.entity.User

@TypeConverters(Converters::class)
@Database(
    entities = [
        Field::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)

abstract class FieldDatabase: RoomDatabase() {

    abstract fun fieldDao(): FieldDao

    companion object {
        @Volatile
        private var INSTANCE: FieldDatabase? = null

        fun getInstance(context: Context): FieldDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FieldDatabase::class.java,
                    "field_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}