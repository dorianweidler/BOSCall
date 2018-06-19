package de.boscall.services

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import de.boscall.dao.AlarmDao
import de.boscall.dto.Alarm
import de.boscall.util.DateTypeConverter

@Database(entities = arrayOf(Alarm::class), version = 2, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        private var INSTANCE: AlarmDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE alarm ADD COLUMN date INTEGER DEFAULT 0 NOT NULL;")

            }
        }

        fun getInstance(context: Context): AlarmDatabase {
            if (INSTANCE == null) {
                synchronized(AlarmDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AlarmDatabase::class.java, "alarm.db")
                            .addMigrations(AlarmDatabase.MIGRATION_1_2)
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