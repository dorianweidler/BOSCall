package de.boscall.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import de.boscall.util.DateTypeConverter


@Entity()
data class Alarm(@ColumnInfo(name = "title") var title: String, @ColumnInfo(name = "text") var text: String, @ColumnInfo(name = "date") var date: Date) {
    @PrimaryKey(autoGenerate = true)
    var aid: Int? = null
}