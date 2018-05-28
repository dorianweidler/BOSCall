package de.boscall.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import de.boscall.dto.Date

@Dao

interface DateDao {

    @Query("SELECT * FROM date ORDER by did DESC")
    fun getAll(): LiveData<MutableList<Date>>

    @Insert(onConflict = REPLACE)
    fun insert(date: Date)

    @Delete()
    fun delete(date: Date)

    @Query("DELETE FROM date")
    fun deleteAll()
}