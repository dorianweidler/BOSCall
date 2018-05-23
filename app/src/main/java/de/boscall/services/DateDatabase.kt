package de.boscall.services

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import de.boscall.dao.DateDao
import de.boscall.dto.Date

@Database(entities = arrayOf(Date::class), version = 1)
abstract class DateDatabase : RoomDatabase() {

    abstract fun dateDao(): DateDao

    companion object {
        private var INSTANCE: DateDatabase? = null

        fun getInstance(context: Context): DateDatabase {
            if (INSTANCE == null) {
                synchronized(DateDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            DateDatabase::class.java, "date.db")
                            .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}