package de.boscall.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Alarm(@ColumnInfo(name = "title") var title: String, @ColumnInfo(name = "text") var text: String) {
    @PrimaryKey(autoGenerate = true)
    var aid: Int? = null
}