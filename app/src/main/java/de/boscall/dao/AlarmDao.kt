package de.boscall.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import de.boscall.dto.Alarm

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Insert(onConflict = REPLACE)
    fun insert(alarm: Alarm)

    @Delete()
    fun delete(alarm: Alarm)
}