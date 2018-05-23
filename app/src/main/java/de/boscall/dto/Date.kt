package de.boscall.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Date(@ColumnInfo(name = "title") var title: String, @ColumnInfo(name = "text") var text: String, var date: String) {
    @PrimaryKey(autoGenerate = true)
    var did: Int? = null

    fun getFormatedDate() : String{
        var newStr = date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4)

        return newStr
    }
}