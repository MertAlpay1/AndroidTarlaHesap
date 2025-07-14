package com.example.tarlauygulamasi.data.locale.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tarlauygulamasi.data.dao.UserDao
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
    version = 2,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {

    abstract fun fieldDao(): FieldDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "database"
                ).fallbackToDestructiveMigration(true).build()
                //.fallbackToDestructiveMigration(true)
                INSTANCE = instance
                instance
            }
        }
    }
}